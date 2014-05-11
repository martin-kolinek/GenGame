package org.kolinek.gengame.util

import rx.lang.scala.Observable
import rx.lang.scala.Scheduler
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import rx.subjects.BehaviorSubject
import rx.lang.scala.JavaConversions._
import rx.lang.scala.Subject
import rx.lang.scala.Observer
import scala.util.Success
import scala.util.Failure
import scala.util.Try

trait RXHelper {
    implicit class toBehavior[T](obs: Observable[T]) {
        def toBehavior(init: T): Observable[T] = {
            val subj = BehaviorSubject.create(init)
            obs.subscribe(subj)
            subj.asObservable
        }
    }

    implicit class withLatest[T](obs: Observable[T]) {
        def withLatest[R](other: Observable[R]) = {
            other.map(x => obs.map(y => (y, x))).switch
        }
    }

    implicit class collectPartFunc[T](obs: Observable[T]) {
        def collectPartFunc[R](func: PartialFunction[T, R]) = {
            obs.filter(func.isDefinedAt).map(func)
        }
    }

    implicit class ObservableInference[T](obs: Observable[T]) {
        def scanI(f: (T, T) => T) = obs.scan(f)
    }

    trait IsFuture[F, T] {
        def onComplete(t: F, func: Try[T] => Unit)
    }

    implicit def futureIsFuture[T](implicit ec: ExecutionContext) = new IsFuture[Future[T], T] {
        def onComplete(t: Future[T], func: Try[T] => Unit) = t.onComplete(func)
    }

    implicit class ObservableFuture[F, T](obs: Observable[F])(implicit ev: IsFuture[F, T]) {
        def removeFuture: Observable[T] = {
            val subj = Subject[T]
            obs.subscribe(new Observer[F] {
                override def onCompleted() {
                    subj.onCompleted()
                }
                override def onNext(fut: F) {
                    ev.onComplete(fut, {
                        case Success(x) => subj.onNext(x)
                        case Failure(thr) => subj.onError(thr)
                    })
                }
                override def onError(error: Throwable) {
                    subj.onError(error)
                }
            })
            subj
        }
    }
}