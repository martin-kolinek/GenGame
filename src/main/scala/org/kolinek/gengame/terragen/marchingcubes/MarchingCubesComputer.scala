package org.kolinek.gengame.terragen.marchingcubes

import org.kolinek.gengame.geometry._
import spire.syntax.all._
import org.kolinek.gengame.terragen.TerragenDefinition
import org.kolinek.gengame.terragen.TerragenDefinitionProvider

class MarchingCubesComputer(defin: TerragenDefinition) {
    private val marchCubes = MarchCubeCases.getAllMarchingCubeCases

    def compute(chunk: Chunk) = {
        val points = new PointGrid(Cube.unit :* (Chunk.chunkSize + 1.cube), makeNormalizedFunc(chunk.lower, defin.func))
        val edges = new EdgeGrid(points, defin.treshold)
        def edgePt(offset: SingleCube, edg: Int) = new EdgePoint(chunk.lower + offset, edg)
        val tris = for {
            pt <- Chunk.offsets
            goodPoints = for (p <- 0 to 7; if points.getValue(pt, p) > defin.treshold) yield p
            goodSet = goodPoints.toSet
            triangles = marchCubes(goodSet)
            (a, b, c) <- triangles
        } yield (edges.getPoint(pt, a),
            edges.getPoint(pt, b),
            edges.getPoint(pt, c))
        val allPts = (for ((a, b, c) <- tris) yield Seq(a, b, c)).flatten.distinct.toIndexedSeq
        val indMap = allPts.zipWithIndex.toMap
        val indexes = for ((a, b, c) <- tris)
            yield (indMap(a), indMap(b), indMap(c))
        new ComputedChunk(chunk, allPts, indexes.map(Triangle.tupled))
    }

    private def makeNormalizedFunc(origin: SingleCube, func: SingleCube => Double): SingleCube => Double = {
        return x => func(x + origin)
    }
}

trait MarchingCubesComputerProvider {
    def marchingCubesComputer: MarchingCubesComputer
}

trait DefaultMarchingCubesComputerProvider extends MarchingCubesComputerProvider {
    self: TerragenDefinitionProvider =>
    lazy val marchingCubesComputer = new MarchingCubesComputer(terragenDefinition)
}