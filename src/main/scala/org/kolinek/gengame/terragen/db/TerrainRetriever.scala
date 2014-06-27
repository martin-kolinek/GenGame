package org.kolinek.gengame.terragen.db

import org.kolinek.gengame.terragen.LocalTerrainPiecesProvider
import org.kolinek.gengame.terragen.ToGenerateProvider
import slick.driver.SQLiteDriver.simple._
import org.kolinek.gengame.geometry.Chunk
import org.kolinek.gengame.util._
import rx.lang.scala.Observable
import org.kolinek.gengame.terragen.CurrentTerrainChunks
import scala.concurrent.duration._
import org.kolinek.gengame.util.collectPartFunc
import org.kolinek.gengame.terragen.TerrainChunkLoad
import org.kolinek.gengame.terragen.TerrainChunkUnload
import org.kolinek.gengame.terragen.TerrainGeneratorProvider
import org.kolinek.gengame.terragen.LoadTerrainPiece
import org.kolinek.gengame.terragen.UnloadTerrainPiece
import org.kolinek.gengame.terragen.SavedTerrainPieceAction

class TerrainRetriever(savedTerrainPieceCreator: Observable[SavedTerrainPieceCreator])(implicit session: Session) extends TerragenTables {
    def retrieve(chunk: Chunk): Option[Observable[SavedTerrainPiece]] = {
        val doneChunksQuery = doneChunksTable.filter(ch => ch.x === chunk.x && ch.y === chunk.y && ch.z === chunk.z)
        if (doneChunksQuery.list.isEmpty) {
            None
        } else {
            val q = for {
                ch <- doneChunksQuery
                mesh <- meshesTable if ch.id === mesh.chunkId
            } yield mesh.meshes
            Some(Observable.from(q.list.map { mesh =>
                savedTerrainPieceCreator.map(_.createTerrainPiece(mesh.data, mesh.id))
            }).flatten)
        }
    }

    def retrieveMultiple(chunks: Observable[Chunk]): Observable[(Chunk, Option[Observable[SavedTerrainPiece]])] = {
        chunks.map(ch => ch -> retrieve(ch))
    }
}

trait TerrainRetrieverComponent extends LocalTerrainPiecesProvider with ToGenerateProvider {
    self: DatabaseProvider with CurrentTerrainChunks with SavedTerrainPieceCreatorProvider with TerrainGeneratorProvider =>

    lazy val (localTerrainPieces, toGenerate) = {
        val loadUnloads = terrainChunkActions.groupByUntil(_.chunk, { (_: Chunk, obs) =>
            obs.collectPartFunc {
                case TerrainChunkUnload(ch) => ch
            }
        })

        val fromDb = loadUnloads.map(_._2).buffer(500.milliseconds).flatMap { chunks =>
            database.flatMap { db =>
                db.withSession { implicit s =>
                    val retriever = new TerrainRetriever(savedTerrainPieceCreator)
                    retriever.retrieveMultiple(Observable.from(chunks).flatten.map(_.chunk))
                }
            }
        }

        val generate = fromDb.collectPartFunc {
            case (chunk, None) => chunk
        }

        val chunkPieces = fromDb.collectPartFunc {
            case (chunk, Some(pieces)) => chunk -> pieces
        }

        val actionObservables = for {
            ((ch, loadUnloads), (_, retrievals)) <- loadUnloads.joinNextFrom(chunkPieces)(_._1 == _._1)
        } yield {
            val unload = loadUnloads.collectPartFunc {
                case TerrainChunkUnload(ch) => ch
            }
            val loads: Observable[SavedTerrainPieceAction] = retrievals.map(LoadTerrainPiece)
            val unloads: Observable[SavedTerrainPieceAction] = retrievals.replay.map(UnloadTerrainPiece)
            loads.takeUntil(unload) ++ unloads
        }
        
        (actionObservables.flatten, generate)
    }
}