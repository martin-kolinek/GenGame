package org.kolinek.gengame.reporting

import com.typesafe.scalalogging.slf4j.LazyLogging

trait ErrorLogger {
    def logError(thr: Throwable)
}

class GlobalErrorLogger extends ErrorLogger with LazyLogging {
    def logError(thr: Throwable) = logger.error("Unhandled error occurred", thr)
}

trait ErrorLoggingComponent {
    def errorLogger: ErrorLogger
}

trait DefaultErrorLoggingComponent extends ErrorLoggingComponent {
    def errorLogger = new GlobalErrorLogger
}