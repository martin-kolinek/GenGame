package org.kolinek.gengame.terragen

import org.kolinek.gengame.geometry._
import rx.lang.scala.Observable
import spire.syntax.all._

trait CurrentTerrainChunks {
    def terrainChunkActions: Observable[TerrainChunkAction]
}

trait DefaultCurrentTerrainChunks extends CurrentTerrainChunks {
    self: VisitedChunksProvider =>

    class TerrainChunkBuffer private (bufferSize: ChunkUnit, val originalChunk: Option[Chunk], val previousChunk: Option[Chunk]) {
        def this(bufferSize: ChunkUnit) = this(bufferSize, None, None)
        def around(ch: Chunk) = for {
            x <- ch.x - bufferSize to ch.x + bufferSize
            y <- ch.y - bufferSize to ch.y + bufferSize
            z <- ch.z - bufferSize to ch.z + bufferSize
        } yield Point(x, y, z)

        def changes: Iterable[TerrainChunkAction] = {
            (previousChunk, originalChunk) match {
                case (None, Some(originalChunk)) => around(originalChunk).map(TerrainChunkLoad)
                case (Some(previousChunk), Some(originalChunk)) => {
                    val aroundPrev = around(previousChunk).toSet
                    val aroundOrig = around(originalChunk).toSet
                    (aroundOrig -- aroundPrev).map(TerrainChunkLoad) ++
                        (aroundPrev -- aroundOrig).map(TerrainChunkUnload)
                }
                case _ => Nil
            }
        }

        def changesObservable = Observable.from(changes)

        def nextChunk(ch: Chunk) = new TerrainChunkBuffer(bufferSize, Some(ch), originalChunk)
    }

    lazy val terrainChunkActions = visitedChunks.scan(new TerrainChunkBuffer(3.chunk))(_ nextChunk _).flatMap(_.changesObservable).share
}

trait TerrainChunkAction {
    val chunk: Chunk
}

case class TerrainChunkLoad(chunk: Chunk) extends TerrainChunkAction

case class TerrainChunkUnload(chunk: Chunk) extends TerrainChunkAction