package org.kolinek.gengame.terragen.marchingcubes

import org.kolinek.gengame.geometry._
import spire.syntax.all._

object EdgeIDTranslation {
    def getOffset(edgeId: Int) = edgeId match {
        case 0 => Point(0.cube, 0.cube, 0.cube)
        case 1 => Point(1.cube, 0.cube, 0.cube)
        case 2 => Point(0.cube, 1.cube, 0.cube)
        case 3 => Point(0.cube, 0.cube, 0.cube)
        case 4 => Point(0.cube, 0.cube, 0.cube)
        case 5 => Point(1.cube, 0.cube, 0.cube)
        case 6 => Point(1.cube, 1.cube, 0.cube)
        case 7 => Point(0.cube, 1.cube, 0.cube)
        case 8 => Point(0.cube, 0.cube, 1.cube)
        case 9 => Point(1.cube, 0.cube, 1.cube)
        case 10 => Point(0.cube, 1.cube, 1.cube)
        case 11 => Point(0.cube, 0.cube, 1.cube)
    }

    def getDirection(edgeId: Int) = edgeId match {
        case x if x <= 7 && x >= 4 => EdgeDirection.Z
        case x if x % 2 == 0 => EdgeDirection.X
        case _ => EdgeDirection.Y
    }
}

case class EdgePoint(val pos: SingleCube, val dir: EdgeDirection.Value) {
    def this(pos: SingleCube, edgeid: Int) =
        this(pos + EdgeIDTranslation.getOffset(edgeid), EdgeIDTranslation.getDirection(edgeid))

}
