package org.kolinek.gengame.terragen.db

import org.kolinek.gengame.geometry._
import rx.lang.scala.Observable
import org.kolinek.gengame.terragen.TerrainGenerator
import org.kolinek.gengame.util._

trait TerrainLoader {
    def loadTerrain(chunks: Observable[Chunk]): Observable[SavedChunk]
}

trait TerrainLoaderProvider {
    def terrainLoader: TerrainLoader
}

class DefaultTerrainLoader(retriever: TerrainRetriever, generator: TerrainGenerator, saver: TerrainPieceSaver) extends TerrainLoader {
    def loadTerrain(chunks: Observable[Chunk]) = {
        val chunkRetrievals = retriever.chunkRetrievals(chunks)
        val piecesFromDb = chunkRetrievals.collectPartFunc {
            case ChunkGenerated(SavedChunk(chunk, pieces)) => SavedChunk(chunk, pieces)
        }
        val generatedChunks = generator.generateChunks(chunkRetrievals.collectPartFunc {
            case ChunkNotGenerated(chunk) => chunk
        })
        val generatedSavedPieces = saver.savedTerrainPieces(generatedChunks)

        piecesFromDb merge generatedSavedPieces
    }
}