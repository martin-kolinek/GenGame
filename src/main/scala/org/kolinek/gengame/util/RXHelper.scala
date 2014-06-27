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

    implicit class JoinNextOps[T, R](obs: Observable[T]) {
        def joinNextFrom(obs2: Observable[R])(equality: (T, R) => Boolean) = {
            obs.map { t =>
                obs2.filter(r => equality(t, r)).take(1).map(r => (t, r))
            }.flatten
        }
    }
}