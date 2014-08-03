package org.kolinek.gengame.util

import scala.concurrent.Future
import java.util.concurrent.ConcurrentHashMap
import scala.collection.JavaConversions._
import java.util.concurrent.ConcurrentLinkedQueue
import scala.collection.concurrent.Map
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.mutable.HashMap
import scala.collection.mutable.Queue

trait Memoization {
    implicit class MemoizeOps[T, R](func: (T => R)) {
        def memoize(maxSize: Int) = new (T => R) {
            val map = new HashMap[T, R]
            val q = new Queue[T]
            def apply(t: T) = {
                map.get(t) match {
                    case Some(v) => v
                    case None => {
                        memoized(t, func(t))
                    }
                }

            }

            private def memoized(t: T, r: R) = {
                map(t) = r
                q.enqueue(t)
                if (q.size > maxSize) {
                    val rem = q.dequeue
                    map.remove(rem)
                }
                r
            }
        }
    }
}