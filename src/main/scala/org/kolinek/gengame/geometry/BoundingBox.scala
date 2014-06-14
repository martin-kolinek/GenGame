package org.kolinek.gengame.geometry

import spire.algebra.Ring
import spire.algebra.Order
import spire.syntax.ring._
import spire.syntax.order._
import com.jme3.bounding.{ BoundingBox => JmeBBox }
import spire.math.ConvertableFrom

case class BoundingBox[T: Ring: Order](val min: Point[T], val max: Point[T]) {
    val zero = implicitly[Ring[T]].zero
    def contains(p: Point[T]): Boolean = {
        List(p - min, max - p).forall(x => x.forall(_ >= zero))
    }

    def overlaps(other: BoundingBox[T]): Boolean = {
        List(max - other.min, other.max - min).forall(x => x.forall(_ >= zero))
    }
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

trait BoundingBoxImplicits {
    implicit class BoundingBoxOps[T: ConvertableFrom](bbox: BoundingBox[T]) {
        def toJmeBoundingBox = new JmeBBox(bbox.min.toVector3f, bbox.max.toVector3f)
    }
}