package org.kolinek.gengame

package object geometry
        extends PointNormedVectorSpaceInstance
        with PositionUnitImplicits
        with CubeImplicits
        with IntegralRange {
    type Position = Point[PositionUnit]
    type SingleCube = Point[CubeUnit]
}