package org.kolinek.gengame.main

import org.kolinek.gengame.threading.ErrorHelpers
import org.kolinek.gengame.reporting.ErrorLoggingComponent
import org.kolinek.gengame.game.nifty.SimpleScreenController
import org.kolinek.gengame.threading.GameExecutionHelper
import org.kolinek.gengame.threading.GameExecutionContextComponent
import org.kolinek.gengame.reporting.FPSCounterComponent

trait HudComponent extends ErrorHelpers with GameExecutionHelper {
    self: ErrorLoggingComponent with CameraPositionComponent with GameExecutionContextComponent with FPSCounterComponent =>

    class HudController extends SimpleScreenController {
        lazy val fpsLabel = new NiftyLabel("FPSLabel")
        lazy val positionLabel = new NiftyLabel("PositionLabel")

        def controls = List(fpsLabel, positionLabel)

        def setup() {
            cameraPosition
                .map(p => f"X:${p.x.underlying}%.3f Y:${p.y.underlying}%.3f Z:${p.z.underlying}%.3f")
                .subscribeOnUpdateLoop(positionLabel.setText)

            fps.map(fps => s"FPS: $fps")
                .foreach(fpsLabel.setText)
        }
    }
}