package org.kolinek.gengame.threading

import org.kolinek.gengame.reporting.ErrorLoggingComponent
import rx.lang.scala.Observable
import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure
import java.util.concurrent.Executor
import scala.concurrent.ExecutionContext
import rx.lang.scala.Subscriber

trait ErrorHelpers2 {
    self: ErrorLoggingComponent =>

    object ErrorReportExecutionContext {
        def fromExecutor(exec: Executor) = ExecutionContext.fromExecutor(exec, errorLogger.logError)
    }
}