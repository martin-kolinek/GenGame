package org.kolinek.gengame.geometry

import spire.algebra.Ring
import spire.algebra.Order
import spire.syntax.ring._
import spire.syntax.order._
import com.jme3.bounding.{ BoundingBox => JmeBBox }

case class BoundingBox[T: Ring: Order](val min: Point[T], val max: Point[T]) {
    val zero = implicitly[Ring[T]].zero
    def contains(p: Point[T]): Boolean = {
        List(p - min, max - p).forall(x => x.forall(_ >= zero))
    }

    def overlaps(other: BoundingBox[T]): Boolean = {
        List(max - other.min, other.max - min).forall(x => x.forall(_ >= zero))
    }

    def toJmeBoundingBox(implicit ev1: IsFloatPrecise[T]) = new JmeBBox(min.toVector3f, max.toVector3f)

    def precise[R](implicit ev1: HasBounds[Point[T], Point[R]], ev2: Ring[R], ev3: Order[R]) = BoundingBox(ev1.lower(min), ev1.upper(max))

    def coarse[R](implicit ev1: HasBounds[Point[R], Point[T]], ev2: Ring[R], ev3: Order[R]) = BoundingBox(ev1.bound(min), ev1.bound(max))
}

object BoundingBox {
    def fromPoints[T: Ring: Order](pts: Seq[Point[T]]) = {
        val mins = pts.reduce { (mn, pt) =>
            (mn zip pt).map((implicitly[Order[T]].min _).tupled)
        }
        val maxs = pts.reduce { (mx, pt) =>
            (mx zip pt).map((implicitly[Order[T]].max _).tupled)
        }

        BoundingBox(mins, maxs)
    }
}
