package org.kolinek.gengame.util

import rx.lang.scala.Observable
import rx.lang.scala.Scheduler
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import rx.subjects.BehaviorSubject
import rx.lang.scala.JavaConversions._

trait RXHelper {
    implicit class subscribeFuture[T](obs: Observable[T]) {
        def subscribeFuture(ctx: ExecutionContext)(act: T => Unit) = obs.subscribe(p => Future {
            act(p)
        }(ctx))
    }

    implicit class toBehavior[T](obs: Observable[T]) {
        def toBehavior(init: T): Observable[T] = {
            val subj = BehaviorSubject.create(init)
            obs.subscribe(subj)
            subj.asObservable
        }
    }
}