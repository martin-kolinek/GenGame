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

/*class TerrainRetriever(savedTerrainPieceCreator: Observable[SavedTerrainPieceCreator])(implicit session: Session) extends TerragenTables {
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
}*/

sealed trait ChunkRetrieval

case class ChunkNotGenerated(chunk: Chunk) extends ChunkRetrieval

case class ChunkGenerated(chunk: Chunk, pieces: Seq[SavedTerrainPiece]) extends ChunkRetrieval

trait TerrainRetriever {
    def chunkRetrievals(chunk: Observable[Chunk]): Observable[ChunkRetrieval]
}

trait TerrainRetrieverProvider {
    def terrainRetriever: TerrainRetriever
}

/*class TerrainRetrieveAction extends DatabaseAction[Observable[]]

trait TerrainRetrieverComponent extends LocalTerrainPiecesProvider with ToGenerateProvider {
    self: DatabaseProvider with CurrentTerrainChunks with SavedTerrainPieceCreatorProvider with TerrainGeneratorProvider =>

    lazy val 
    }
}*/