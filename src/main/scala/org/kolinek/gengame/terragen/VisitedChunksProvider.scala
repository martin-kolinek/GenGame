package org.kolinek.gengame.terragen

import rx.lang.scala.Observable
import org.kolinek.gengame.geometry._
import org.kolinek.gengame.main.CameraPositionComponent

trait VisitedChunksProvider {
    def visitedChunks: Observable[Chunk]
}

trait DefaultVisitedChunksProvider extends VisitedChunksProvider {
    self: CameraPositionComponent =>
    lazy val visitedChunks = cameraPosition.map(_.bound.bound).distinctUntilChanged
}

trait VisitedChunksAroundOrigin extends VisitedChunksProvider {
    lazy val visitedChunks = {
        val chunks = for {
            x <- -5.chunk to 5.chunk
            y <- -5.chunk to 5.chunk
            z <- -5.chunk to 5.chunk
        } yield Point(x, y, z)
        Observable.items(chunks: _*)
    }
}