package org.kolinek.gengame.threading

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.util.Try

class BoundFuture[T] private (underlying: Future[T], ec: ExecutionContext) {
    private implicit val ctx = ec

    def map[R](f: T => R) = {
        new BoundFuture(underlying.map(f), ec)
    }

    def flatMap[R](f: T => Future[R]) = {
        new BoundFuture(underlying.flatMap(f), ec)
    }

    def onComplete(f: Try[T] => Unit) = {
        underlying.onComplete(f(_))
    }
}

object BoundFuture {
    def apply[T](ec: ExecutionContext)(act: => T) = {
        val f = Future(act)(ec)
        new BoundFuture(f, ec)
    }
}
