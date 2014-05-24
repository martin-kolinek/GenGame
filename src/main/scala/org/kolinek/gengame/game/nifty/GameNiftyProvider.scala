package org.kolinek.gengame.game.nifty

import org.kolinek.gengame.util.Closeable
import com.jme3.niftygui.NiftyJmeDisplay
import org.kolinek.gengame.threading.ErrorHelpers
import org.kolinek.gengame.reporting.ErrorLoggingComponent
import org.kolinek.gengame.game.AppProvider

trait GameNiftyProvider extends NiftyProvider with Closeable with ErrorHelpers {
    self: AppProvider with ErrorLoggingComponent =>

    lazy val niftyDisplay = (for (a <- app) yield {
        val str = Thread.currentThread().getStackTrace().map(_.toString()).mkString("\n")
        val disp = new NiftyJmeDisplay(a.getAssetManager(), a.getInputManager(), a.getAudioRenderer(), a.getGuiViewPort())
        a.getGuiViewPort.addProcessor(disp)
        disp
    }).cache

    lazy val nifty = niftyDisplay.map(_.getNifty)

    abstract override def close() = {
        super.close()
        for {
            a <- app
            d <- niftyDisplay
        } {
            a.getGuiViewPort.removeProcessor(d)
        }
    }
}