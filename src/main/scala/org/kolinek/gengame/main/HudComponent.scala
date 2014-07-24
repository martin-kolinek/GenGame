package org.kolinek.gengame.main

import org.kolinek.gengame.game.nifty.SimpleScreenController
import org.kolinek.gengame.threading.GameExecutionContextComponent
import org.kolinek.gengame.reporting.FPSCounterComponent
import org.kolinek.gengame.terragen.VisitedChunksProvider

trait HudComponent {
    self: CameraPositionComponent with GameExecutionContextComponent with FPSCounterComponent with VisitedChunksProvider =>

    class HudController extends SimpleScreenController {
        lazy val fpsLabel = new NiftyLabel("FPSLabel")
        lazy val positionLabel = new NiftyLabel("PositionLabel")
        lazy val currentChunkLabel = new NiftyLabel("CurrentChunkLabel")

        def controls = List(fpsLabel, positionLabel)

        def setup() {
            cameraPosition
                .map(p => f"X:${p.x.underlying}%.3f Y:${p.y.underlying}%.3f Z:${p.z.underlying}%.3f")
                .observeOn(gameScheduler)
                .foreach(positionLabel.setText)

            fps.map(fps => s"FPS: $fps")
                .foreach(fpsLabel.setText)

            visitedChunks
                .map(ch => s"CH: X:${ch.x.underlying} Y:${ch.y.underlying} Z:${ch.z.underlying}")
                .foreach(currentChunkLabel.setText)
        }
    }
}