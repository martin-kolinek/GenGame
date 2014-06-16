package org.kolinek.gengame.terragen.db

import org.kolinek.gengame.terragen.LocalTerrainPiecesProvider
import org.kolinek.gengame.terragen.TerrainPiece
import slick.driver.SQLiteDriver.simple._
import com.jme3.asset.AssetManager
import com.jme3.export.binary.BinaryExporter
import java.io.ByteArrayOutputStream
import org.kolinek.gengame.geometry._
import rx.lang.scala.Observable
import org.kolinek.gengame.terragen.GeneratedTerrainPiecesComponent
import scala.concurrent.duration._
import org.kolinek.gengame.threading.ErrorHelpers
import org.kolinek.gengame.reporting.ErrorLoggingComponent
import org.kolinek.gengame.terragen.GeneratedChunk

class TerrainPieceSaver(savedTerrainPieceCreator: Observable[SavedTerrainPieceCreator])(implicit session: Session) extends TerragenTables {

    val writer = new BinaryExporter

    def saveTerrainPiece(genChunk: GeneratedChunk) = {
        Observable.items(genChunk.pieces: _*).flatMap { piece =>
            val mesh = piece.toJmeMesh
            val stream = new ByteArrayOutputStream
            writer.save(mesh, stream)
            val data = stream.toByteArray
            val chunkId = (doneChunksTable returning doneChunksTable.map(_.id)).insert((genChunk.chunk.x, genChunk.chunk.y, genChunk.chunk.z))
            val meshId = (meshesTable returning meshesTable.map(_.id)).insert(data, chunkId)
            savedTerrainPieceCreator.map(_.createTerrainPiece(data, meshId))
        }
    }
}

trait SavedTerrainPiecesComponent {
    def savedTerrainPieces: Observable[SavedTerrainPiece]
}

trait TerrainPieceSaverComponent extends SavedTerrainPiecesComponent with ErrorHelpers {
    self: GeneratedTerrainPiecesComponent with ErrorLoggingComponent with DatabaseProvider with SavedTerrainPieceCreatorProvider =>

    lazy val savedTerrainPieces = generatedTerrainPieces.buffer(500.milliseconds).flatMap { chunks =>
        database.flatMap { db =>
            db.withTransaction { implicit session =>
                val saver = new TerrainPieceSaver(savedTerrainPieceCreator)
                Observable.items(chunks: _*).subscribeOn(databaseScheduler).flatMap(saver.saveTerrainPiece)
            }
        }
    }
}