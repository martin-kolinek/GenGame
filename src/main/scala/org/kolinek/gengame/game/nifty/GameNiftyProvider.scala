package org.kolinek.gengame.game.nifty

import org.kolinek.gengame.util.Closeable
import com.jme3.niftygui.NiftyJmeDisplay
import org.kolinek.gengame.threading.AppProvider

trait GameNiftyProvider extends NiftyProvider with Closeable {
    self: AppProvider =>

    lazy val niftyDisplay = for (a <- app) yield {
        val disp = new NiftyJmeDisplay(a.getAssetManager(), a.getInputManager(), a.getAudioRenderer(), a.getGuiViewPort())
        a.getGuiViewPort.addProcessor(disp)
        disp
    }

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