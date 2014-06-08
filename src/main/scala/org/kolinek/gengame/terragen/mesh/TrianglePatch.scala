package org.kolinek.gengame.terragen.mesh

import org.kolinek.gengame.geometry._
import org.kolinek.gengame.terragen.TerrainPiece

class TrianglePatch(val tris: Seq[Triangle], val area: TriangleArea) {
    def toTerrainPiece = {
        val allPoints = tris.flatMap(_.toList)
        val points = allPoints.distinct.toIndexedSeq
        val pointPositions = points.zipWithIndex.toMap
        val indexes = allPoints.map(pointPositions)
        val normals = points.map(area.pointNormal)
        new TerrainPiece(points.map(area.positions), indexes.to[scala.collection.immutable.Seq], normals)
    }
}