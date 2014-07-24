package org.kolinek.gengame.main

import org.kolinek.gengame.reporting.ErrorLoggingComponent
import org.kolinek.gengame.reporting.FPSCounterComponent
import org.kolinek.gengame.threading.GameExecutionContextComponent
import org.kolinek.gengame.game.nifty.NiftyProvider
import org.kolinek.gengame.terragen.VisitedChunksProvider

trait MainNiftyModule
        extends HudComponent
        with MainNiftyComponent {
    self: ErrorLoggingComponent with CameraPositionComponent with FPSCounterComponent with GameExecutionContextComponent with NiftyProvider with VisitedChunksProvider =>
}