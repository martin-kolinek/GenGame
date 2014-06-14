package org.kolinek.gengame.geometry

import deriving.deriving
import spire.math.Integral

class CubeUnit(val underlying: Long) extends AnyVal {
    override def toString = s"$underlying.cube"
}

trait CubeImplicits {
    implicit val CubeUnitIsIntegral = deriving[CubeUnit, Integral].equiv(_.underlying, (x: Long) => new CubeUnit(x))

    implicit val cubeUnitHasBounds = new HasBounds[CubeUnit, PositionUnit] {
        def upper(c: CubeUnit) = (c.underlying + 1).pos
        def lower(c: CubeUnit) = c.underlying.pos
        def bound(p: PositionUnit) = p.underlying.toLong.cube
    }

    implicit class CubeUnitFromLong(l: Long) {
        def cube = new CubeUnit(l)
    }
}

object Cube extends ((CubeUnit, CubeUnit, CubeUnit) => Point[CubeUnit]) {
    def apply(x: CubeUnit, y: CubeUnit, z: CubeUnit) = Point(x, y, z)

    def zero = Point(0.cube, 0.cube, 0.cube)

    def unit = Point(1.cube, 1.cube, 1.cube)
}