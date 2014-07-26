package org.kolinek.gengame.terragen.db

import org.kolinek.gengame.geometry._
import rx.lang.scala.Observable
import org.kolinek.gengame.terragen.TerrainGenerator
import org.kolinek.gengame.util._
import org.kolinek.gengame.terragen.TerrainGeneratorProvider
import com.typesafe.scalalogging.slf4j.LazyLogging

trait TerrainLoader {
    def loadTerrain(chunks: Observable[Chunk]): Observable[SavedChunk]
}

trait TerrainLoaderProvider {
    def terrainLoader: TerrainLoader
}

class DefaultTerrainLoader(retriever: TerrainRetriever, generator: TerrainGenerator, saver: TerrainPieceSaver) extends TerrainLoader with LazyLogging {
    def loadTerrain(chunks: Observable[Chunk]) = {
        val chunkRetrievals = retriever.chunkRetrievals(chunks)
        val piecesFromDb = chunkRetrievals.collectPartFunc {
            case ChunkGenerated(SavedChunk(chunk, pieces)) => {
                SavedChunk(chunk, pieces)
            }
        }
        val generatedChunks = generator.generateChunks(chunkRetrievals.collectPartFunc {
            case ChunkNotGenerated(chunk) => {
                chunk
            }
        })

        val generatedSavedPieces = saver.savedTerrainPieces(generatedChunks)

        (piecesFromDb merge generatedSavedPieces).share
    }
}

trait DefaultTerrainLoaderProvider extends TerrainLoaderProvider {
    self: TerrainRetrieverProvider with TerrainGeneratorProvider with TerrainPieceSaverProvider =>

    lazy val terrainLoader = new DefaultTerrainLoader(terrainRetriever, terrainGenerator, terrainPieceSaver)
}