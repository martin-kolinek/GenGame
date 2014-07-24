package org.kolinek.gengame.main

import org.kolinek.gengame.game.nifty.NiftyProvider
import org.kolinek.gengame.reporting.FPSCounterComponent
import org.kolinek.gengame.threading.GameExecutionContextComponent
import org.kolinek.gengame.terragen.VisitedChunksProvider

trait MainNiftyComponent extends HudComponent {
    self: NiftyProvider with CameraPositionComponent with GameExecutionContextComponent with FPSCounterComponent with VisitedChunksProvider =>

    lazy val hudController = new HudController

    nifty.foreach(_.fromXml("gui/main/gui.xml", "hud", hudController))
}