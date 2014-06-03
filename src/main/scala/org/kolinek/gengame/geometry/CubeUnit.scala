package org.kolinek.gengame.geometry

import deriving.deriving
import spire.math.Integral

class CubeUnit(val underlying: Long) extends AnyVal {
    override def toString = s"$underlying.cube"
}

trait CubeImplicits {
    implicit val CubeUnitIsIntegral = deriving[CubeUnit, Integral].equiv(_.underlying, (x: Long) => new CubeUnit(x))

    implicit class CubeUnitFromLong(l: Long) {
        def cube = new CubeUnit(l)
    }
}

object Cube extends ((CubeUnit, CubeUnit, CubeUnit) => Point[CubeUnit]) {
    def apply(x: CubeUnit, y: CubeUnit, z: CubeUnit) = Point(x, y, z)

    def zero = Point(0.cube, 0.cube, 0.cube)
}