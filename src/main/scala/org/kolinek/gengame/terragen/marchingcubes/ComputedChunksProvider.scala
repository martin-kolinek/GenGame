package org.kolinek.gengame.terragen.marchingcubes

import rx.lang.scala.Observable

trait ComputedChunksProvider {
    def computedChunks: Observable[ComputedChunk]
}