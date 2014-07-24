package org.kolinek.gengame.terragen

import rx.lang.scala.Observable
import org.kolinek.gengame.geometry._
import org.kolinek.gengame.main.CameraPositionComponent

trait VisitedChunksProvider {
    def visitedChunks: Observable[Chunk]
}

trait DefaultVisitedChunksProvider extends VisitedChunksProvider {
    self: CameraPositionComponent with CurrentTerrainChunks =>
    lazy val visitedChunks = cameraPosition.map(_.bound.bound).distinctUntilChanged.share
}

