package org.kolinek.gengame.db

import slick.driver.SQLiteDriver.simple._
import rx.lang.scala.Subject
import rx.lang.scala.Observable
import rx.lang.scala.subjects.ReplaySubject
import scala.concurrent.duration._
import org.kolinek.gengame.threading.ErrorHelpers
import org.kolinek.gengame.reporting.ErrorLoggingComponent
import rx.schedulers.Schedulers
import rx.lang.scala.JavaConversions._

trait DatabaseAction[T] extends Function[Session, T] {
}

trait DatabaseActionExecutor {
    def executeAction[T](act: DatabaseAction[T]): Observable[T]
}

trait DatabaseActionExecutorProvider {
    def databaseActionExecutor: DatabaseActionExecutor
}

trait BufferDatabaseActionExecutorProvider extends DatabaseActionExecutorProvider with ErrorHelpers {
    self: ErrorLoggingComponent with DatabaseProvider =>

    lazy val databaseActionExecutor = new DatabaseActionExecutor {
        val subject = Subject[Function[Session, Unit]]()

        def executeAction[T](act: DatabaseAction[T]): Observable[T] = {
            val ret = ReplaySubject[T]
            subject.onNext { s =>
                ret.onNext(act(s))
                ret.onCompleted
            }
            ret.subscribeOn(Schedulers.computation())
        }

        subject.buffer(500.milliseconds).foreach { funcs =>
            database.foreach { db =>
                db.withTransaction { session =>
                    funcs.foreach(_(session))
                }
            }
        }
    }
}