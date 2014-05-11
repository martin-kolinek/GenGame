package org.kolinek.gengame.threading

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.util.Try
import rx.lang.scala.Observable
import org.kolinek.gengame.util.ObservableFuture
import org.kolinek.gengame.util.IsFuture

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

trait BoundFutureImplicits {
    implicit def boundFutureIsFuture[T] = new IsFuture[BoundFuture[T], T] {
        def onComplete(f: BoundFuture[T], func: Try[T] => Unit) {
            f.onComplete(func)
        }
    }
}