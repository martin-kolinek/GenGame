package org.kolinek.gengame.terragen

import rx.lang.scala.Observable
import rx.lang.scala.JavaConversions._
import org.kolinek.gengame.geometry._
import rx.schedulers.Schedulers

case class GeneratedChunk(chunk: Chunk, pieces: Seq[TerrainPiece])

trait GeneratedTerrainPiecesComponent {
    def generatedTerrainPieces: Observable[GeneratedChunk]
}

trait DefaultGeneratedTerrainPiecesComponent extends GeneratedTerrainPiecesComponent {
    self: TerrainGeneratorProvider with ToGenerateProvider =>

    lazy val generatedTerrainPieces = toGenerate.observeOn(Schedulers.computation())
        .map { chunk =>
            val pieces = terrainGenerator.generateChunk(chunk)
            GeneratedChunk(chunk, pieces)
        }

}