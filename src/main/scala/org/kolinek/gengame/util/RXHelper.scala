package org.kolinek.gengame.util

import rx.lang.scala.Observable
import rx.lang.scala.Scheduler
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import rx.subjects.BehaviorSubject
import rx.lang.scala.JavaConversions._

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
}