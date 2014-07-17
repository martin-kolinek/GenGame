package org.kolinek.gengame.terragen

import rx.lang.scala.Observable
import org.kolinek.gengame.geometry._
import org.kolinek.gengame.util._
import scala.concurrent.duration._
import org.kolinek.gengame.main.CameraPositionComponent
import rx.schedulers.Schedulers
import rx.lang.scala.JavaConversions._
import org.kolinek.gengame.terragen.db.SavedTerrainPiece
import org.kolinek.gengame.terragen.db.TerrainLoaderProvider

trait LocalTerrainPiecesProvider {
    def localTerrainPieces: Observable[SavedTerrainPieceAction]
}

sealed trait SavedTerrainPieceAction {
    val savedMesh: SavedTerrainPiece
}

case class LoadTerrainPiece(savedMesh: SavedTerrainPiece) extends SavedTerrainPieceAction

case class UnloadTerrainPiece(savedMesh: SavedTerrainPiece) extends SavedTerrainPieceAction

trait DbLocalTerrainPiecesProvider extends LocalTerrainPiecesProvider {
    self: CurrentTerrainChunks with TerrainLoaderProvider =>
    lazy val localTerrainPieces = {
        val loadUnloads = terrainChunkActions.groupByUntil(_.chunk, { (ch: Chunk, _) =>
            terrainChunkActions.collectPartFunc {
                case TerrainChunkUnload(ch2) if ch2 == ch => ch
            }
        })

        val toLoad = terrainChunkActions.collectPartFunc {
            case TerrainChunkLoad(ch) => ch
        }

        val loaded = terrainLoader.loadTerrain(toLoad)

        val actionObservables = for {
            ((ch, loadUnloads), retrievals) <- loadUnloads.joinNextFrom(loaded)(_._1 == _.chunk)
        } yield {
            val unload = loadUnloads.collectPartFunc {
                case TerrainChunkUnload(ch) => ch
            }
            val loads = retrievals.pieces.map(LoadTerrainPiece)
            val unloads = retrievals.pieces.map(UnloadTerrainPiece).replay
            unload.foreach { _ =>
                unloads.connect
            }

            loads ++ unloads
        }

        actionObservables.flatten
    }
}