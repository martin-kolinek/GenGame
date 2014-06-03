package org.kolinek.gengame.geometry

import spire.math.Integral
import spire.syntax.integral._
import scala.runtime.RichLong

trait IntegralRange {
    implicit class IntegralRangeOps[T: Integral](i: T) {
        def to(o: T) = {
            ((i.toLong: RichLong) to o.toLong).map(implicitly[Integral[T]].fromLong)
        }

        def until(o: T) = {
            ((i.toLong: RichLong) until o.toLong).map(implicitly[Integral[T]].fromLong)
        }
    }
}