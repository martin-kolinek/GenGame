package org.kolinek.gengame.terragen.marchingcubes

import org.kolinek.gengame.geometry._

case class ComputedChunk(ch: Chunk, pts: IndexedSeq[(EdgePoint, Position)], tris: Seq[(Int, Int, Int)]) 