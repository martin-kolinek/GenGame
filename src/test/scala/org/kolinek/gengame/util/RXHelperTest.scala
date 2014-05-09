package org.kolinek.gengame.util

import org.scalatest.FunSuite
import rx.subjects.PublishSubject
import rx.lang.scala.Observable
import rx.lang.scala.JavaConversions._
import scala.collection.mutable.ListBuffer

class RXHelperTest extends FunSuite {
    test("withLatest works") {
        val s1 = PublishSubject.create[Int]
        val s2 = PublishSubject.create[Int]
        val o1: Observable[Int] = s1
        val o2: Observable[Int] = s2
        val comb = o1.withLatest(o2)
        val occurrences = new ListBuffer[(Int, Int)]
        comb.subscribe(x => occurrences += x)
        s1.onNext(1)
        s2.onNext(2)
        s1.onNext(3)
        s2.onNext(4)
        s2.onNext(5)
        s2.onNext(6)
        s1.onNext(7)
        s1.onNext(8)
        assert(occurrences.toList === List(3 -> 2, 7 -> 6, 8 -> 6))
    }
}