package org.kolinek.gengame.geometry

import deriving.deriving
import spire.syntax.integral._
import spire.math.Integral

class ChunkUnit(val underlying: Long) extends AnyVal {
    override def toString = s"$underlying.chunk"
}

trait ChunkImplicits {
    implicit val chunkIsIntegral = deriving[ChunkUnit, Integral].equiv(_.underlying, (x: Long) => new ChunkUnit(x))

    implicit val chunkUnitHasBounds = new HasBounds[ChunkUnit, CubeUnit] {
        def lower(c: ChunkUnit) = (c.underlying.cube * Chunk.chunkSize)
        def upper(c: ChunkUnit) = ((c.underlying + 1).cube * Chunk.chunkSize - 1)
        def bound(cb: CubeUnit) = if (cb.underlying >= 0)
            (cb.underlying / Chunk.chunkSize.underlying).chunk
        else
            (~(~cb.underlying / Chunk.chunkSize.underlying)).chunk;
    }

    implicit val chunkUnitHasCenter = new HasCenter[ChunkUnit, PositionUnit] {
        def center(c: ChunkUnit) = (c.lower.lower.underlying + (Chunk.chunkSize.underlying / 2)).pos
    }

    implicit class ChunkUnitFromLong(l: Long) {
        def chunk = new ChunkUnit(l)
    }
}

object Chunk extends ((ChunkUnit, ChunkUnit, ChunkUnit) => Point[ChunkUnit]) {
    def apply(x: ChunkUnit, y: ChunkUnit, z: ChunkUnit) = Point(x, y, z)

    def zero = apply(0.chunk, 0.chunk, 0.chunk)

    def unit = apply(1.chunk, 1.chunk, 1.chunk)

    val chunkSize: CubeUnit = 32.cube

    def offsets = {
        for {
            x <- 0.cube until chunkSize
            y <- 0.cube until chunkSize
            z <- 0.cube until chunkSize
        } yield Point(x, y, z)
    }
}