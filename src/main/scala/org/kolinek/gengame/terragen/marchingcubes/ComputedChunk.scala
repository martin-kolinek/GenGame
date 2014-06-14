package org.kolinek.gengame.terragen.marchingcubes

import org.kolinek.gengame.geometry._
import spire.syntax.all._

class ComputedChunk(ch: Chunk, pts: IndexedSeq[Position], val tris: Seq[Triangle]) {
    val positions = pts.map(_ + ch.lower.lower)

} 