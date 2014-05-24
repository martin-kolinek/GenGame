package org.kolinek.gengame.threading

import org.kolinek.gengame.reporting.ErrorLoggingComponent
import rx.lang.scala.Observable
import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure

trait ErrorHelpers {
    self: ErrorLoggingComponent =>

    implicit class ObservableErrorOps[T](obs: Observable[T]) {
        def foreach(f: T => Unit) = obs.subscribe(f, errorLogger.logError _)
    }
}