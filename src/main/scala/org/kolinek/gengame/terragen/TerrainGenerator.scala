package org.kolinek.gengame.terragen

import org.kolinek.gengame.geometry._
import org.kolinek.gengame.terragen.mesh.MeshProcessor
import org.kolinek.gengame.terragen.marchingcubes.MarchingCubesComputer
import org.kolinek.gengame.terragen.mesh.TriangleArea
import org.kolinek.gengame.terragen.marchingcubes.MarchingCubesComputerProvider
import org.kolinek.gengame.terragen.mesh.MeshProcessorProvider
import com.typesafe.scalalogging.slf4j.LazyLogging
import rx.lang.scala.Observable

trait TerrainGenerator {
    def generateChunk(chunk: Chunk): Seq[TerrainPiece]
}

class DefaultTerrainGenerator(marchingCubesComputer: MarchingCubesComputer, meshProcessor: MeshProcessor) extends TerrainGenerator with LazyLogging {
    def generateChunk(chunk: Chunk) = {
        logger.debug(s"Generating chunk $chunk")
        val cubes = marchingCubesComputer.compute(chunk)
        logger.debug(s"Generated cubes")
        val area = new TriangleArea(cubes.tris, cubes.positions)
        logger.debug("Generated area")
        val terrainPieces = meshProcessor(area)
        logger.debug("Generated terrain pieces")
        terrainPieces
    }
}

trait TerrainGeneratorProvider {
    def terrainGenerator: TerrainGenerator
}

trait DefaultTerrainGeneratorProvider extends TerrainGeneratorProvider {
    self: MarchingCubesComputerProvider with MeshProcessorProvider =>

    lazy val terrainGenerator = new DefaultTerrainGenerator(marchingCubesComputer, meshProcessor)
}

trait ToGenerateProvider {
    def toGenerate: Observable[Chunk]
}

trait 