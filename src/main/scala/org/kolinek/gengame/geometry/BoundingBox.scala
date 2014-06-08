package org.kolinek.gengame.geometry

import spire.algebra.Ring
import spire.algebra.Order
import spire.syntax.ring._
import spire.syntax.order._

case class BoundingBox[T: Ring: Order](val min: Point[T], val max: Point[T]) {
    val zero = implicitly[Ring[T]].zero
    def contains(p: Point[T]): Boolean = {
        List(p - min, max - p).forall(x => x.forall(_ >= zero))
    }

    def overlaps(other: BoundingBox[T]): Boolean = {
        List(max - other.min, other.max - min).forall(x => x.forall(_ >= zero))
    }
}
