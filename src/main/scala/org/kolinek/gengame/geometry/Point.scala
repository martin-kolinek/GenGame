package org.kolinek.gengame.geometry

import spire.algebra._
import com.jme3.math.Vector3f
import spire.syntax.all._

case class Point[T](x: T, y: T, z: T) {
    def map[R](f: T => R) = Point(f(x), f(y), f(z))
    def foldLeft[R](r: R)(f: (R, T) => R) = f(f(f(r, x), y), z)
    def forall(f: T => Boolean) = foldLeft(true)(_ && f(_))
    def zip[R](o: Point[R]) = Point(x -> o.x, y -> o.y, z -> o.z)
    def toList = List(x, y, z)
    def reduce(f: (T, T) => T) = f(f(x, y), z)
    def toVector3f(implicit ev: IsFloatPrecise[T]) = new Vector3f(ev.toFloat(x), ev.toFloat(y), ev.toFloat(z))
    def toBoundingBox(implicit ev1: Ring[T], ev2: Order[T]) = BoundingBox(this, this)
}

object Point {
    def fromList[T](l: List[T]) = l match {
        case x :: y :: z :: Nil => Point(x, y, z)
        case _ => throw new AssertionError
    }
}

trait PointModuleInstance {
    class PointIsModule[T](implicit ev: Ring[T]) extends Module[Point[T], T] {
        import spire.syntax.ring._
        // Members declared in spire.algebra.AdditiveGroup   
        def negate(x: Point[T]) = Point(-x.x, -x.y, -x.z)
        // Members declared in spire.algebra.AdditiveMonoid   
        def zero: Point[T] = Point(ev.zero, ev.zero, ev.zero)
        // Members declared in spire.algebra.AdditiveSemigroup   
        def plus(x: Point[T], y: Point[T]) = Point(x.x + y.x, x.y + y.y, x.z + y.z)
        // Members declared in spire.algebra.Module   
        def timesl(r: T, v: Point[T]) = Point(r * v.x, r * v.y, r * v.z)
        // Members declared in spire.algebra.VectorSpace   
        def scalar = ev
    }

    implicit def pointIsModule[T: Ring] = new PointIsModule[T]
}

trait PointVectorSpaceInstance extends PointModuleInstance {

    class PointIsInnerProductSpace[T](implicit ev: Field[T]) extends PointIsModule[T] with InnerProductSpace[Point[T], T] {
        import spire.syntax.field._
        override def scalar = ev
        def dot(v: Point[T], w: Point[T]) = v.x * w.x + v.y * w.y + v.z * w.z
    }

    implicit def pointIsInnerProductSpace[T: Field]: InnerProductSpace[Point[T], T] = new PointIsInnerProductSpace[T]
}

trait PointNormedVectorSpaceInstance extends PointVectorSpaceInstance {
    implicit def pointIsNormedVectorSpace[T: Field: NRoot]: NormedVectorSpace[Point[T], T] = pointIsInnerProductSpace[T].normed
}
