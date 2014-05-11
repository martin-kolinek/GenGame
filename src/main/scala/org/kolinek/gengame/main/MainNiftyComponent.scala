package org.kolinek.gengame.main

import org.kolinek.gengame.game.nifty.NiftyProvider
import org.kolinek.gengame.threading.ErrorHelpers
import org.kolinek.gengame.reporting.ErrorLoggingComponent
import org.kolinek.gengame.reporting.FPSCounterComponent
import org.kolinek.gengame.threading.GameExecutionContextComponent

trait MainNiftyComponent extends ErrorHelpers with HudComponent {
    self: NiftyProvider with ErrorLoggingComponent with CameraPositionComponent with GameExecutionContextComponent with FPSCounterComponent =>

    lazy val hudController = new HudController

    nifty.foreach(_.fromXml("gui/main/gui.xml", "hud", hudController))
}