package org.kolinek.gengame.util

import org.scalatest.FunSuite
import rx.subjects.PublishSubject
import rx.lang.scala.Observable
import rx.lang.scala.JavaConversions._
import scala.collection.mutable.ListBuffer
import rx.lang.scala.Subject

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

    test("joinNextFrom works") {
        val s1 = Subject[Int]()
        val s2 = Subject[Int]()
        val comb = s1.joinNextFrom(s2)(_ == _)

        val occurrences = new ListBuffer[(Int, Int)]
        comb.subscribe(x => occurrences += x)

        s1.onNext(1)
        s2.onNext(1)
        s1.onNext(2)
        s2.onNext(2)
        s1.onNext(3)
        s1.onNext(4)
        s2.onNext(3)
        s2.onNext(4)
        s1.onNext(5)
        s1.onNext(6)
        s2.onNext(6)
        s2.onNext(5)
        s1.onNext(1)
        s2.onNext(1)
        assert(occurrences.toSet === (1 to 6).map(x => x -> x).toSet)
        assert(occurrences.count(p => p == (1, 1)) === 2)
    }

    test("joinNextFrom dispatches before finish") {
        val s1 = Subject[Int]
        val s2 = Subject[Int]

        val comb = s1.joinNextFrom(s2)(_ == _)

        var first = false
        comb.foreach {
            case (1, 1) => assert(!first)
            case (2, 2) => assert(first)
        }

        s1.onNext(1)
        s2.onNext(1)
        first = true
        s1.onNext(2)
        s2.onNext(2)
    }
}