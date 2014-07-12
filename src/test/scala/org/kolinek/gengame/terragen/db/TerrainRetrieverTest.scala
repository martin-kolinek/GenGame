package org.kolinek.gengame.terragen.db

import org.scalatest.FunSuite
import org.kolinek.gengame.db.DatabaseActionExecutorProvider
import rx.lang.scala.Observable
import rx.lang.scala.subjects.ReplaySubject
import org.kolinek.gengame.db.DatabaseAction
import org.kolinek.gengame.db.DatabaseActionExecutor
import org.kolinek.gengame.geometry._
import org.kolinek.gengame.db.BufferDatabaseActionExecutorProvider
import org.kolinek.gengame.db.InMemoryDatabaseProvider
import org.kolinek.gengame.reporting.DefaultErrorLoggingComponent
import slick.driver.SQLiteDriver.simple._
import org.kolinek.gengame.db.schema.TerragenTables
import org.kolinek.gengame.db.schema.DefaultSchemaCreatorProvider
import org.kolinek.gengame.util.DefaultOnCloseProvider
import org.kolinek.gengame.db.SingleSessionDatabaseActionExecutorProvider

class TerrainRetrieverTest extends FunSuite {
    class TestTerrainPieceCreator extends SavedTerrainPieceCreator {
        def createTerrainPiece(data: Array[Byte], id: Long) = new SavedTerrainPiece(null, id)
    }

    class TestComp extends SingleSessionDatabaseActionExecutorProvider
            with InMemoryDatabaseProvider
            with DefaultErrorLoggingComponent
            with DefaultSchemaCreatorProvider
            with TerragenTables
            with DefaultOnCloseProvider {

        val chunk = databaseActionExecutor.executeAction(DatabaseAction { implicit s =>
            val chunk = (doneChunksTable returning doneChunksTable.map(_.id)) += (1.chunk, 1.chunk, 1.chunk)
            meshesTable.insert(Array(), chunk)
            chunk
        })
    }

    test("TerrainRetrieverAction works for existing chunk") {
        val existChunk = Chunk(1.chunk, 1.chunk, 1.chunk)
        val comp = new TestComp
        val testExisting = comp.databaseActionExecutor.executeAction(
            new TerrainRetrieveAction(
                existChunk,
                Observable.items(new TestTerrainPieceCreator)))
        assert(testExisting.toBlocking.single match {
            case ChunkGenerated(SavedChunk(existChunk, pieces)) => {
                lazy val piece = pieces.toBlocking.head
                pieces.toBlocking.toList.size == 1 &&
                    piece.id == comp.chunk.toBlocking.single &&
                    piece.geom == null
            }
            case _ => false
        })
        comp.close()
    }

    test("TerrainRetrieverAction works for non existent chunk") {
        val nonExistChunk = Chunk(1.chunk, 1.chunk, 3.chunk)
        val comp = new TestComp
        val testNonExisting = comp.databaseActionExecutor.executeAction(
            new TerrainRetrieveAction(
                nonExistChunk,
                Observable.items(new TestTerrainPieceCreator)))

        assert(testNonExisting.toBlocking.single === ChunkNotGenerated(nonExistChunk))
        comp.close()
    }

    class RetrieverComp extends DefaultTerrainRetriverProvider
            with InMemoryDatabaseProvider
            with SingleSessionDatabaseActionExecutorProvider
            with DefaultErrorLoggingComponent
            with DefaultSchemaCreatorProvider
            with SavedTerrainPieceCreatorProvider
            with DefaultOnCloseProvider {
        lazy val savedTerrainPieceCreator = Observable.items(new TestTerrainPieceCreator)
    }

    test("DefaultTerrainRetriever checks all chunks") {
        val retriverComp = new RetrieverComp
        val chunks = Seq(Chunk(1.chunk, 1.chunk, 1.chunk),
            Chunk(2.chunk, 2.chunk, 2.chunk),
            Chunk(-5.chunk, -1.chunk, -4.chunk))

        val retrievals = retriverComp.terrainRetriever.chunkRetrievals(Observable.items(chunks: _*))

        assert(retrievals.toBlocking.toList.collect {
            case ChunkNotGenerated(ch) => ch
        }.toSet === chunks.toSet)

    }
}