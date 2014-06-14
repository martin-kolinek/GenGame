package org.kolinek.gengame.terragen

import rx.lang.scala.Observable
import org.kolinek.gengame.main.CameraPositionComponent
import rx.schedulers.Schedulers
import rx.lang.scala.JavaConversions._

trait LocalTerrainPiecesProvider {
    def localTerrainPieces: Observable[TerrainPiece]
}

trait DefaultTerrainPiecesProvider extends LocalTerrainPiecesProvider {
    self: TerrainGeneratorProvider with VisitedChunksProvider =>

    lazy val localTerrainPieces =
        for {
            chunk <- visitedChunks.distinct.observeOn(Schedulers.computation())
            piece <- Observable.items(terrainGenerator.generateChunk(chunk): _*)
        } yield piece
}