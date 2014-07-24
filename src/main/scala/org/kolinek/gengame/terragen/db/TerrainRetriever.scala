package org.kolinek.gengame.terragen.db

import org.kolinek.gengame.terragen.LocalTerrainPiecesProvider
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
import org.kolinek.gengame.db.DatabaseProvider
import org.kolinek.gengame.db.DatabaseAction
import org.kolinek.gengame.db.DatabaseActionExecutor
import org.kolinek.gengame.db.DatabaseActionExecutorProvider
import org.kolinek.gengame.db.schema.TerragenTables

sealed trait ChunkRetrieval

case class ChunkNotGenerated(chunk: Chunk) extends ChunkRetrieval

case class ChunkGenerated(chunk: SavedChunk) extends ChunkRetrieval

trait TerrainRetriever {
    def chunkRetrievals(chunks: Observable[Chunk]): Observable[ChunkRetrieval]
}

trait TerrainRetrieverProvider {
    def terrainRetriever: TerrainRetriever
}

class TerrainRetrieveAction(chunk: Chunk, savedTerrainPieceCreator: Observable[SavedTerrainPieceCreator]) extends DatabaseAction[ChunkRetrieval] with TerragenTables {
    def apply(session: Session) = {
        val doneChunksQuery = doneChunksTable.filter(ch => ch.x === chunk.x && ch.y === chunk.y && ch.z === chunk.z)
        if (doneChunksQuery.list(session).isEmpty) {
            ChunkNotGenerated(chunk)
        } else {
            val q = for {
                ch <- doneChunksQuery
                mesh <- meshesTable if ch.id === mesh.chunkId
            } yield mesh.meshes
            val pieces = Observable.from(q.list(session).map { mesh =>
                savedTerrainPieceCreator.map(_.createTerrainPiece(mesh.data, mesh.id))
            }).flatten
            ChunkGenerated(SavedChunk(chunk, pieces))
        }
    }
}

class DefaultTerrainRetriever(exec: DatabaseActionExecutor, savedTerrainPieceCreator: Observable[SavedTerrainPieceCreator]) extends TerrainRetriever {
    def chunkRetrievals(chunks: Observable[Chunk]) = {
        chunks.flatMap { ch =>
            exec.executeAction(new TerrainRetrieveAction(ch, savedTerrainPieceCreator))
        }.share
    }
}

trait DefaultTerrainRetriverProvider extends TerrainRetrieverProvider {
    self: SavedTerrainPieceCreatorProvider with DatabaseActionExecutorProvider =>

    lazy val terrainRetriever = new DefaultTerrainRetriever(databaseActionExecutor, savedTerrainPieceCreator)
}
