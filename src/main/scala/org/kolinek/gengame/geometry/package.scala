package org.kolinek.gengame

package object geometry
        extends PointNormedVectorSpaceInstance
        with PositionUnitImplicits {
    type Position = Point[PositionUnit]
}