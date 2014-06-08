package org.kolinek.gengame.terragen

import rx.lang.scala.Observable

trait TerrainPieceProvider {
    def terrainPieces: Observable[TerrainPiece]
}