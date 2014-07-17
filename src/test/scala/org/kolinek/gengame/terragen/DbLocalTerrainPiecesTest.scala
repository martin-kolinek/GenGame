package org.kolinek.gengame.terragen

import org.scalatest.FunSuite
import org.kolinek.gengame.terragen.db.TerrainLoaderProvider
import org.kolinek.gengame.terragen.db.TerrainLoader
import rx.lang.scala.Observable
import org.kolinek.gengame.geometry._
import org.kolinek.gengame.terragen.db.SavedTerrainPiece
import org.kolinek.gengame.terragen.db.SavedChunk
import scala.concurrent.duration._
import org.kolinek.gengame.reporting.DefaultErrorLoggingComponent
import scala.collection.mutable.ListBuffer

class DbLocalTerrainPiecesTest extends FunSuite {
    class TestComp
            extends DbLocalTerrainPiecesProvider
            with TerrainLoaderProvider
            with CurrentTerrainChunks
            with DefaultErrorLoggingComponent {
        def terrainLoader = new TerrainLoader {
            def loadTerrain(chunks: Observable[Chunk]) = chunks.map { ch =>
                //println(s"Creating savedchunk for $ch")
                SavedChunk(ch, chunkPieceObservables(ch))
            }
        }

        val actions = Seq(TerrainChunkLoad(Chunk(0.chunk, 0.chunk, 0.chunk)),
            TerrainChunkLoad(Chunk(1.chunk, 0.chunk, 0.chunk)),
            TerrainChunkLoad(Chunk(2.chunk, 0.chunk, 0.chunk)),
            TerrainChunkUnload(Chunk(0.chunk, 0.chunk, 0.chunk)),
            TerrainChunkLoad(Chunk(3.chunk, 0.chunk, 0.chunk)),
            TerrainChunkUnload(Chunk(1.chunk, 0.chunk, 0.chunk)),
            TerrainChunkUnload(Chunk(2.chunk, 0.chunk, 0.chunk)),
            TerrainChunkUnload(Chunk(3.chunk, 0.chunk, 0.chunk)))

        val chunkPieces = actions.map(_.chunk).distinct.zipWithIndex.map {
            case (ch, i) => ch -> new SavedTerrainPiece(null, i)
        }.toMap

        val chunkPieceObservables = chunkPieces.map {
            case (ch, p) => ch -> Observable.items(p)
        }

        val terrainChunkActions = Observable.from(actions).replay
    }

    test("DbLocalTerrainPieces works") {
        val comp = new TestComp
        val terrainPieceActions = comp.localTerrainPieces
        val listBuffer = new ListBuffer[SavedTerrainPieceAction]
        terrainPieceActions.subscribe(listBuffer += _)
        comp.terrainChunkActions.connect

        Thread.sleep(10)
        assert(listBuffer.toList === comp.actions.map {
            case TerrainChunkLoad(ch) => LoadTerrainPiece(comp.chunkPieces(ch))
            case TerrainChunkUnload(ch) => UnloadTerrainPiece(comp.chunkPieces(ch))
        })
    }
}