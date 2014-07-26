package org.kolinek.gengame

package object geometry
        extends PointNormedVectorSpaceInstance
        with PositionUnitImplicits
        with CubeImplicits
        with IntegralRange
        with ChunkImplicits
        with HasCenterImplicits
        with HasBoundsImplicits {
    type Position = Point[PositionUnit]
    type SingleCube = Point[CubeUnit]
    type Chunk = Point[ChunkUnit]
    type BBoxPosition = BoundingBox[PositionUnit]
}