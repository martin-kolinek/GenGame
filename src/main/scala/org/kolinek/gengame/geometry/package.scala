package org.kolinek.gengame

package object geometry
        extends PointNormedVectorSpaceInstance
        with PositionUnitImplicits
        with CubeImplicits
        with IntegralRange
        with ChunkImplicits
        with HasBoundsImplicits
        with PointImplicits
        with BoundingBoxImplicits {
    type Position = Point[PositionUnit]
    type SingleCube = Point[CubeUnit]
    type Chunk = Point[ChunkUnit]
    type BBoxPosition = BoundingBox[PositionUnit]
}