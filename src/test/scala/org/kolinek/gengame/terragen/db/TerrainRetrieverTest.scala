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
import org.kolinek.gengame.threading.ErrorHelpers

class TerrainRetrieverTest extends FunSuite {
    class TestTerrainPieceCreator extends SavedTerrainPieceCreator {
        def createTerrainPiece(data: Array[Byte], id: Long) = new SavedTerrainPiece(null, id)
    }

    /*class TerrainRetrieverComponent extends DefaultTerrainRetriverProvider
    with {

    }*/

    /*object DatabaseComponent extends BufferDatabaseActionExecutorProvider
            with InMemoryDatabaseProvider
            with DefaultErrorLoggingComponent
            with ErrorHelpers
            with TerragenTables {
        val chunk = database.map(db => db.withSession { implicit s =>
            println("creating chunks")
            val chunk = (doneChunksTable returning doneChunksTable.map(_.id)) += (1.chunk, 1.chunk, 1.chunk)
            meshesTable.insert(Array(), chunk)
            chunk
        })
    }

    test("TerrainRetrieverAction works for existing chunk") {
        val existChunk = Chunk(1.chunk, 1.chunk, 1.chunk)

        println("executing existing")
        /*val testExisting = DatabaseComponent.databaseActionExecutor.executeAction(
            new TerrainRetrieveAction(
                existChunk,
                Observable.items(new TestTerrainPieceCreator)))*/
        Thread.sleep(1000)
        info(DatabaseComponent.chunk.toBlocking.single.toString)
        /*assert(testExisting.toBlocking.single match {
            case ChunkGenerated(SavedChunk(existChunk, pieces)) => {
                lazy val piece = pieces.toBlocking.head
                pieces.toBlocking.toList.size == 1 &&
                    piece.id == DatabaseComponent.chunk.toBlocking.single &&
                    piece.geom == null
            }
            case _ => false
        })*/
    }

    /*test("TerrainRetrieverAction works for non existent chunk") {
        val nonExistChunk = Chunk(1.chunk, 1.chunk, 3.chunk)

        println("executing not existing")
        val testNonExisting = DatabaseComponent.databaseActionExecutor.executeAction(
            new TerrainRetrieveAction(
                nonExistChunk,
                Observable.items(new TestTerrainPieceCreator)))

        assert(testNonExisting.toBlocking.single === ChunkNotGenerated(nonExistChunk))
    }*/

    test("DefaultTerrainRetriever works") {
        /*val retriverComp = new TestComp
        retriverComp.terrainRetriever.chunkRetrievals(Observable.items(
            Chunk(1.chunk, 1.chunk, 1.chunk),
            Chunk(2.chunk, 2.chunk, 2.chunk),
            Chunk(-5.chunk, -1.chunk, -4.chunk)))
*/
    }*/
}