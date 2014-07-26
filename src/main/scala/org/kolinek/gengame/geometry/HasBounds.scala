package org.kolinek.gengame.geometry

import spire.math.ConvertableTo

trait HasBounds[B, T] {
    def upper(b: B): T
    def lower(b: B): T

    def bound(t: T): B
}

trait HasCenter[B, T] {
    def center(b: B): T
}

trait HasBoundsImplicits {
    implicit class BoundsOps[B, T](b: B)(implicit ev: HasBounds[B, T]) {
        def upper = ev.upper(b)
        def lower = ev.lower(b)
    }

    implicit class BoundedOps[B, T](t: T)(implicit ev: HasBounds[B, T]) {
        def bound = ev.bound(t)
    }

    implicit def pointHasBounds[B, T](implicit ev: HasBounds[B, T]) = new HasBounds[Point[B], Point[T]] {
        def lower(b: Point[B]) = b.map(_.lower)
        def upper(b: Point[B]) = b.map(_.upper)
        def bound(t: Point[T]) = t.map(_.bound)
    }
}

trait HasCenterImplicits {

    implicit class CenterOps[B, T](b: B)(implicit ev: HasCenter[B, T]) {
        def center = ev.center(b)
    }

    implicit def pointHasCenter[B, T](implicit ev: HasCenter[B, T]) = new HasCenter[Point[B], Point[T]] {
        def center(b: Point[B]) = b.map(_.center)
    }
}