package org.kolinek.gengame.threading

import org.scalatest.FunSuite
import org.kolinek.gengame.reporting.ErrorLogger
import org.kolinek.gengame.reporting.ErrorLoggingComponent
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import rx.lang.scala.Subject

class TestErrorLogger extends ErrorLogger {
    var err: Option[Throwable] = None
    def logError(thr: Throwable) {
        err = Some(thr)
    }
}

trait TestErrorLoggingComponent extends ErrorLoggingComponent {
    val errorLogger = new TestErrorLogger
}

class CurrentThreadExecContext extends ExecutionContext {
    def execute(r: Runnable) = r.run()
    def reportFailure(cause: Throwable) = {}
}

class ErrorHelpersTest extends FunSuite {
    val curThread = new CurrentThreadExecContext
    test("BoundFuture foreach works") {
        val comp = new TestErrorLoggingComponent with ErrorHelpers {
            val f = BoundFuture[Int](curThread) {
                throw new Exception
                1
            }
            f.foreach(x => ())
        }

        assert(comp.errorLogger.err.isDefined)
    }

    test("Observable foreach works") {
        val comp = new TestErrorLoggingComponent with ErrorHelpers {
            val subj = Subject[Int]
            subj.foreach(x => ())
            subj.onError(new Exception)
        }

        assert(comp.errorLogger.err.isDefined)
    }

    test("BoundFuture foreach works when error is thrown in foreach") {
        val comp = new TestErrorLoggingComponent with ErrorHelpers {
            val f = BoundFuture[Int](curThread)(1)
            f.foreach {
                x => throw new Exception
            }
        }

        assert(comp.errorLogger.err.isDefined)
    }

    test("Observable foreach works when error is thrown in foreach") {
        val comp = new TestErrorLoggingComponent with ErrorHelpers {
            val subj = Subject[Int]
            subj.foreach {
                x => throw new Exception
            }
            subj.onNext(1)
        }

        assert(comp.errorLogger.err.isDefined)
    }
}