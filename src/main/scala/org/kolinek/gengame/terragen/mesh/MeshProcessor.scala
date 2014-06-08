package org.kolinek.gengame.terragen.mesh

import org.kolinek.gengame.geometry._
import org.kolinek.gengame.terragen.TerrainPiece

trait MeshProcessor extends ((TriangleArea, BBoxPosition) => (Seq[TerrainPiece], Seq[Triangle])) {
    val maxSize: ChunkUnit
}