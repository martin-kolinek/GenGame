package org.kolinek.gengame.terragen

import rx.lang.scala.Observable
import org.kolinek.gengame.main.CameraPositionComponent
import rx.schedulers.Schedulers
import rx.lang.scala.JavaConversions._
import org.kolinek.gengame.terragen.db.SavedTerrainPiece

trait LocalTerrainPiecesProvider {
    def localTerrainPieces: Observable[SavedTerrainPieceAction]
}

sealed trait SavedTerrainPieceAction {
    val savedMesh: SavedTerrainPiece
}

case class LoadTerrainPiece(savedMesh: SavedTerrainPiece) extends SavedTerrainPieceAction

case class UnloadTerrainPiece(savedMesh: SavedTerrainPiece) extends SavedTerrainPieceAction

