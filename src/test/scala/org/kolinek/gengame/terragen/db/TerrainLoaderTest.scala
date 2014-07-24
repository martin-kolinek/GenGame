package org.kolinek.gengame.terragen.db

import org.scalatest.FunSuite
import org.kolinek.gengame.terragen.TerrainGeneratorProvider
import org.kolinek.gengame.terragen.TerrainGenerator
import rx.lang.scala.Observable
import org.kolinek.gengame.geometry._
import org.kolinek.gengame.terragen.GeneratedChunk
import scala.collection.mutable.HashMap
import scala.collection.mutable.HashSet

class TerrainLoaderTest extends FunSuite {
    class TestComp
            extends DefaultTerrainLoaderProvider
            with TerrainRetrieverProvider
            with TerrainGeneratorProvider
            with TerrainPieceSaverProvider {
        val initiallyGeneratedChunks = (for {
            x <- -1.chunk to 1.chunk
        } yield Chunk(x, 0.chunk, 0.chunk)).toSet

        val chunksGenerated = new HashSet[Chunk]
        val chunksSaved = new HashSet[Chunk]

        val terrainRetriever = new TerrainRetriever {
            def chunkRetrievals(chunks: Observable[Chunk]) = chunks.map { ch =>
                if (initiallyGeneratedChunks.contains(ch))
                    ChunkGenerated(SavedChunk(ch, Observable.empty))
                else
                    ChunkNotGenerated(ch)
            }
        }

        val terrainGenerator = new TerrainGenerator {
            def generateChunk(chunk: Chunk) = if (initiallyGeneratedChunks.contains(chunk) ||
                chunksGenerated.contains(chunk)) {
                fail("generating generated chunk")
            } else {
                chunksGenerated += chunk
                Nil
            }
        }

        val terrainPieceSaver = new TerrainPieceSaver {
            def savedTerrainPieces(genChunks: Observable[GeneratedChunk]) = genChunks.map { ch =>
                if (initiallyGeneratedChunks.contains(ch.chunk) ||
                    chunksSaved.contains(ch.chunk) ||
                    !chunksGenerated.contains(ch.chunk))
                    fail("saving generated chunk")
                else {
                    chunksSaved += ch.chunk
                    SavedChunk(ch.chunk, Observable.empty)
                }
            }
        }
    }

    /*test("DefaultTerrainLoader works") {
        val comp = new TestComp
        val chunks = for {
            x <- -3.chunk to 3.chunk
            y <- -3.chunk to 3.chunk
            z <- -3.chunk to 3.chunk
        } yield Chunk(x, y, z)
        val chunkObs = Observable.from(chunks)
        val result = comp.terrainLoader.loadTerrain(chunkObs).toBlocking.toList
        assert(result.map(_.chunk).toSet === chunks.toSet)
        assert(comp.chunksGenerated === comp.chunksSaved)
        assert(comp.chunksGenerated === chunks.toSet -- comp.initiallyGeneratedChunks.toSet)
    }*/
}