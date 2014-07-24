package org.kolinek.gengame.db

import slick.driver.SQLiteDriver.simple._
import rx.lang.scala.Subject
import rx.lang.scala.Observable
import rx.lang.scala.subjects.ReplaySubject
import scala.concurrent.duration._
import org.kolinek.gengame.reporting.ErrorLoggingComponent
import rx.schedulers.Schedulers
import org.kolinek.gengame.util.OnCloseProvider
import com.sun.xml.internal.ws.Closeable
import scala.concurrent.ExecutionContext
import java.util.concurrent.Executors
import scala.concurrent.Future
import org.kolinek.gengame.threading.SingleThreadedScheduler
import rx.lang.scala.Subscriber
import java.util.concurrent.RejectedExecutionException
import org.kolinek.gengame.db.schema.SchemaCreatorProvider
import org.kolinek.gengame.threading.ErrorHelpers2

trait DatabaseAction[T] extends Function[Session, T] {
}

object DatabaseAction {
    def apply[T](f: Session => T) = new DatabaseAction[T] {
        def apply(s: Session) = f(s)
    }
}

trait DatabaseActionExecutor {
    def executeAction[T](act: DatabaseAction[T]): Observable[T]

    def executeObsAction[T](act: DatabaseAction[Observable[T]]) = executeAction(act).flatten
}

trait DatabaseActionExecutorProvider {
    def databaseActionExecutor: DatabaseActionExecutor
}

trait DatabaseseActionExecutorWithSchemaCreation extends DatabaseActionExecutorProvider {
    self: SchemaCreatorProvider =>
    final lazy val databaseActionExecutor = {
        val exec = databaseActionExecutorWithoutPrep
        exec.executeAction(schemaCreator)
        exec
    }
    protected def databaseActionExecutorWithoutPrep: DatabaseActionExecutor
}

trait BufferDatabaseActionExecutorProvider extends DatabaseseActionExecutorWithSchemaCreation with ErrorHelpers2 {
    self: ErrorLoggingComponent with DatabaseProvider with SchemaCreatorProvider with OnCloseProvider =>

    lazy val databaseActionExecutorWithoutPrep = new DatabaseActionExecutor with Closeable {
        val subject = Subject[Function[Session, Unit]]()
        val executor = Executors.newSingleThreadScheduledExecutor()
        val execCtx = ErrorReportExecutionContext.fromExecutor(executor)

        def executeAction[T](act: DatabaseAction[T]): Observable[T] = {
            val ret = ReplaySubject[T]
            subject.onNext { s =>
                ret.onNext(act(s))
                ret.onCompleted
            }
            ret
        }

        subject.buffer(500.milliseconds).foreach { funcs =>
            if (funcs.size > 0) {
                Future {
                    database.withTransaction { session =>
                        funcs.foreach(_(session))
                    }
                }(execCtx).onFailure {
                    case e: Exception => {
                        errorLogger.logError(e)

                    }
                }(execCtx)
            }
        }

        def close() {
            subject.onCompleted()
            executor.shutdown()
        }
    }

    onClose.foreach { _ =>
        databaseActionExecutorWithoutPrep.close()
    }
}

trait SingleSessionDatabaseActionExecutorProvider extends DatabaseseActionExecutorWithSchemaCreation with ErrorHelpers2 {
    self: ErrorLoggingComponent with DatabaseProvider with SchemaCreatorProvider with OnCloseProvider =>

    lazy val databaseActionExecutorWithoutPrep = new DatabaseActionExecutor with Closeable {
        private lazy val session = database.createSession
        val executor = Executors.newSingleThreadScheduledExecutor()
        val execCtx = ErrorReportExecutionContext.fromExecutor(executor)

        def executeAction[T](act: DatabaseAction[T]): Observable[T] = {
            val subj = ReplaySubject[T]
            Future {
                subj.onNext(act(session))
                subj.onCompleted()
            }(execCtx).onFailure {
                case e: Exception => errorLogger.logError(e)
            }(execCtx)
            subj
        }

        def close() = {
            session.close()
            executor.shutdown()
        }
    }

    onClose.foreach { _ =>
        databaseActionExecutorWithoutPrep.close()
    }
}