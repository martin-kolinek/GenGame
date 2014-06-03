package org.kolinek.gengame.geometry

import deriving.deriving
import spire.algebra._
import spire.math.{ ConvertableFrom, ConvertableTo }
import spire.std.double.DoubleAlgebra
import com.jme3.math.Vector3f

class PositionUnit(val underlying: Double) extends AnyVal {
    override def toString = s"$underlying.pos"
}

trait PositionUnitImplicits {
    implicit val positionUnitIsField = deriving[PositionUnit, Field].equiv(_.underlying, (x: Double) => new PositionUnit(x))
    implicit val positionUnitIsNRoot = deriving[PositionUnit, NRoot].equiv(_.underlying, (x: Double) => new PositionUnit(x))
    implicit val positionUnitIsReal = deriving[PositionUnit, IsReal].equiv(_.underlying, (x: Double) => new PositionUnit(x))
    implicit val positionUnitIsConvertableFrom = deriving[PositionUnit, ConvertableFrom].equiv(_.underlying, (x: Double) => new PositionUnit(x))
    implicit val positionUnitIsConvertableTo = deriving[PositionUnit, ConvertableTo].equiv(_.underlying, (x: Double) => new PositionUnit(x))

    implicit class IntPosition(i: Int) {
        def pos = new PositionUnit(i)
    }

    implicit class LongPosition(l: Long) {
        def pos = new PositionUnit(l)
    }

    implicit class FloatPosition(f: Float) {
        def pos = new PositionUnit(f)
    }

    implicit class DoublePosition(d: Double) {
        def pos = new PositionUnit(d)
    }

    implicit class PositionOps(p: Point[PositionUnit]) {
        def toJmeVector = new Vector3f(p.x.underlying.toFloat, p.y.underlying.toFloat, p.z.underlying.toFloat)
    }
}

object Position extends ((PositionUnit, PositionUnit, PositionUnit) => Point[PositionUnit]) {
    def apply(x: PositionUnit, y: PositionUnit, z: PositionUnit) = Point(x, y, z)

    def zero = Point(0.pos, 0.pos, 0.pos)
}