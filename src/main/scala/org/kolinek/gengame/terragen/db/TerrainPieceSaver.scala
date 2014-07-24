package org.kolinek.gengame.terragen.db

import org.kolinek.gengame.terragen.LocalTerrainPiecesProvider
import org.kolinek.gengame.terragen.TerrainPiece
import slick.driver.SQLiteDriver.simple._
import com.jme3.asset.AssetManager
import com.jme3.export.binary.BinaryExporter
import java.io.ByteArrayOutputStream
import org.kolinek.gengame.geometry._
import rx.lang.scala.Observable
import scala.concurrent.duration._
import org.kolinek.gengame.terragen.GeneratedChunk
import org.kolinek.gengame.db.DatabaseProvider
import org.kolinek.gengame.db.DatabaseActionExecutorProvider
import org.kolinek.gengame.db.DatabaseAction
import org.kolinek.gengame.db.DatabaseActionExecutor
import org.kolinek.gengame.db.schema.TerragenTables
import com.typesafe.scalalogging.slf4j.LazyLogging

class TerrainPieceSaveAction(savedTerrainPieceCreator: Observable[SavedTerrainPieceCreator], piece: TerrainPiece, chunk: Chunk) extends DatabaseAction[Observable[SavedTerrainPiece]] with TerragenTables {

    val writer = new BinaryExporter

    def apply(session: Session) = {
        val mesh = piece.toJmeMesh
        val stream = new ByteArrayOutputStream
        writer.save(mesh, stream)
        val data = stream.toByteArray
        val chunkId = (doneChunksTable returning doneChunksTable.map(_.id)).insert((chunk.x, chunk.y, chunk.z))(session)
        val meshId = (meshesTable returning meshesTable.map(_.id)).insert(data, chunkId)(session)
        savedTerrainPieceCreator.map(_.createTerrainPiece(data, meshId))
    }
}

trait TerrainPieceSaver {
    def savedTerrainPieces(genChunks: Observable[GeneratedChunk]): Observable[SavedChunk]
}

class DefaultTerrainPieceSaver(databaseActionExecutor: DatabaseActionExecutor, savedTerrainPieceCreator: Observable[SavedTerrainPieceCreator]) extends TerrainPieceSaver with LazyLogging {
    def savedTerrainPieces(genChunks: Observable[GeneratedChunk]) =
        genChunks.map { chunk =>
            val saveActions = Observable.from(chunk.pieces).map { piece =>
                new TerrainPieceSaveAction(savedTerrainPieceCreator, piece, chunk.chunk)
            }
            logger.debug(s"Saving terrain pieces for chunk $chunk")
            SavedChunk(chunk.chunk, saveActions.flatMap(databaseActionExecutor.executeObsAction))
        }
}

trait TerrainPieceSaverProvider {
    def terrainPieceSaver: TerrainPieceSaver
}

trait DefaultTerrainPieceSaverProvider extends TerrainPieceSaverProvider {
    self: DatabaseActionExecutorProvider with SavedTerrainPieceCreatorProvider =>

    lazy val terrainPieceSaver = new DefaultTerrainPieceSaver(databaseActionExecutor, savedTerrainPieceCreator)
}
