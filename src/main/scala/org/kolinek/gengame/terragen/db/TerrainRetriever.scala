package org.kolinek.gengame.terragen.db

import org.kolinek.gengame.terragen.LocalTerrainPiecesProvider
import org.kolinek.gengame.terragen.ToGenerateProvider
import slick.driver.SQLiteDriver.simple._
import org.kolinek.gengame.geometry.Chunk
import rx.lang.scala.Observable
import org.kolinek.gengame.terragen.CurrentTerrainChunks
import scala.concurrent.duration._
import org.kolinek.gengame.util.collectPartFunc
import org.kolinek.gengame.terragen.TerrainChunkLoad

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

    def retrieveMultiple(chunks: Seq[Chunk]): Observable[(Chunk, Option[Observable[SavedTerrainPiece]])] = {
        Observable.items(chunks.map(ch => ch -> retrieve(ch)): _*)
    }
}

trait TerrainRetrieverComponent extends LocalTerrainPiecesProvider with ToGenerateProvider {
    self: DatabaseProvider with CurrentTerrainChunks with SavedTerrainPieceCreatorProvider =>

    lazy val (localTerrainPieces, toGenerate) = {
        val addings = terrainChunkActions.collectPartFunc {
            case TerrainChunkLoad(chunk) => chunk
        }
        val retrievals = addings.buffer(500.milliseconds).flatMap { chunks =>
            database.flatMap { db => 
                db.withSession { implicit s =>
                    val retriever = new TerrainRetriever(savedTerrainPieceCreator)
                    retriever.retrieveMultiple(chunks)
                }
            }
        }
        val generate = retrievals.collectPartFunc {
            case (chunk, None) => chunk
        }

        val retrieved = retrievals.collectPartFunc {
            case (chunk, Some(meshes)) => chunk -> meshes
        }
        (???, generate)
    }
}