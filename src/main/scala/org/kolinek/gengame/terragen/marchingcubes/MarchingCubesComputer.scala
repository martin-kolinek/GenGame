package org.kolinek.gengame.terragen.marchingcubes

import org.kolinek.gengame.geometry._
import spire.syntax.all._
import org.kolinek.gengame.terragen.TerragenDefinition

class MarchingCubesComputer(defin: TerragenDefinition) {
    private val marchCubes = MarchCubeCases.getAllMarchingCubeCases

    def compute(chunk: Chunk) = {
        val points = new PointGrid(Chunk.zero.upper + Point(1.cube, 1.cube, 1.cube), makeNormalizedFunc(chunk.lower, defin.func))
        val edges = new EdgeGrid(points, defin.treshold)
        def edgePt(offset: SingleCube, edg: Int) = new EdgePoint(chunk.lower + offset, edg)
        val tris = for {
            pt <- Chunk.offsets
            goodPoints = for (p <- 0 to 7; if points.getValue(pt, p) > defin.treshold) yield p
            goodSet = goodPoints.toSet
            triangles = marchCubes(goodSet)
            (a, b, c) <- triangles
        } yield (edgePt(pt, a) -> edges.getPoint(pt, a),
            edgePt(pt, b) -> edges.getPoint(pt, b),
            edgePt(pt, c) -> edges.getPoint(pt, c))
        val allPts = (for ((a, b, c) <- tris) yield Seq(a, b, c)).flatten.toSet.toIndexedSeq
        val indMap = allPts.zipWithIndex.toMap
        val indexes = for ((a, b, c) <- tris)
            yield (indMap(a), indMap(b), indMap(c))
        ComputedChunk(chunk, allPts, indexes)
    }

    private def makeNormalizedFunc(origin: SingleCube, func: SingleCube => Double): SingleCube => Double = {
        return x => func(x + origin)
    }
}