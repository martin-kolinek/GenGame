package org.kolinek.gengame.terragen

import org.scalatest.FunSuite
import org.kolinek.gengame.db.InMemoryDatabaseProvider
import org.kolinek.gengame.util.DefaultOnCloseProvider
import org.kolinek.gengame.db.schema.DefaultSchemaCreatorProvider
import org.kolinek.gengame.db.SingleSessionDatabaseActionExecutorProvider
import org.kolinek.gengame.terragen.db.DefaultSavedTerrainPieceCreatorProvider
import org.kolinek.gengame.terragen.marchingcubes.DefaultMarchingCubesComputerProvider
import org.kolinek.gengame.terragen.mesh.DefaultMeshProcessorProvider
import org.kolinek.gengame.terragen.db.DefaultTerrainLoaderProvider
import org.kolinek.gengame.terragen.db.DefaultTerrainRetriverProvider
import org.kolinek.gengame.terragen.db.DefaultTerrainPieceSaverProvider
import org.kolinek.gengame.reporting.DefaultErrorLoggingComponent
import org.kolinek.gengame.game.AssetManagerProvider
import com.jme3.asset.AssetManager
import com.jme3.asset.DesktopAssetManager
import rx.lang.scala.Observable
import rx.lang.scala.Subject
import org.kolinek.gengame.geometry._
import org.kolinek.gengame.terragen.db.TerrainPieceSaverProvider
import org.kolinek.gengame.terragen.db.SavedTerrainPieceCreatorProvider
import org.kolinek.gengame.util.DefaultOnCloseProvider
import org.kolinek.gengame.terragen.db.SavedTerrainPieceCreator
import org.kolinek.gengame.terragen.db.SavedTerrainPiece
import org.kolinek.gengame.terragen.db.TerrainPieceSaver
import org.kolinek.gengame.terragen.db.SavedChunk
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.EasyMockSugar
import org.kolinek.gengame.terragen.db.TerrainLoader
import org.kolinek.gengame.terragen.db.TerrainLoaderProvider
import org.kolinek.gengame.terragen.db.SavedChunk
import scala.collection.mutable.ListBuffer

@RunWith(classOf[JUnitRunner])
class TerrainGenerationModuleTest extends FunSuite with EasyMockSugar {
    class TestComp extends InMemoryDatabaseProvider
            with DefaultOnCloseProvider
            with DefaultSchemaCreatorProvider
            with SingleSessionDatabaseActionExecutorProvider
            with SavedTerrainPieceCreatorProvider
            with DefaultCurrentTerrainChunks
            with VisitedChunksProvider
            with DefaultTerrainGeneratorProvider
            with DefaultMarchingCubesComputerProvider
            with DefaultMeshProcessorProvider
            with DefaultTerragenDefinitionProvider
            with DbLocalTerrainPiecesProvider
            with DefaultTerrainLoaderProvider
            with DefaultTerrainRetriverProvider
            with DefaultTerrainPieceSaverProvider
            with DefaultErrorLoggingComponent {

        def savedTerrainPieceCreator = Observable.items(new SavedTerrainPieceCreator {
            def createTerrainPiece(data: Array[Byte], id: Long) = new SavedTerrainPiece(null, id)
        })
        lazy val visitedChunks = Subject[Chunk]
    }

    test("TerrainGenerationModule works") {
        val comp = new TestComp
        val actions = new ListBuffer[SavedTerrainPieceAction]
        comp.localTerrainPieces.foreach(actions += _)

        comp.visitedChunks.onNext(Chunk(0.chunk, 0.chunk, 0.chunk))
        comp.visitedChunks.onNext(Chunk(1.chunk, 0.chunk, 0.chunk))
        comp.visitedChunks.onNext(Chunk(2.chunk, 0.chunk, 0.chunk))
        comp.visitedChunks.onNext(Chunk(2.chunk, 1.chunk, 0.chunk))
        comp.visitedChunks.onCompleted()

        comp.localTerrainPieces.toBlocking.toList
        
        assert(actions.size > 0)
    }
}