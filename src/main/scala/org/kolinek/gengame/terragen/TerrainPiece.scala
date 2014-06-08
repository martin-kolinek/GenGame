package org.kolinek.gengame.terragen

import org.kolinek.gengame.terragen.mesh.NormalCalculator
import org.kolinek.gengame.geometry._

class TerrainPiece(val points: IndexedSeq[Position], val indexes: Seq[Int], val normals: Seq[Position]) {
    def this(points: IndexedSeq[Position], indexes: Seq[Int]) =
        this(points, indexes, NormalCalculator.createNormals(points, indexes))

}
