package org.kolinek.gengame.game.nifty

import org.kolinek.gengame.util.Closeable
import com.jme3.niftygui.NiftyJmeDisplay
import org.kolinek.gengame.game.AppProvider

trait GameNiftyProvider extends NiftyProvider with Closeable {
    self: AppProvider =>

    lazy val niftyDisplay = {
        val disp = new NiftyJmeDisplay(app.getAssetManager(), app.getInputManager(), app.getAudioRenderer(), app.getGuiViewPort())
        app.getGuiViewPort.addProcessor(disp)
        disp
    }

    lazy val nifty = niftyDisplay.getNifty

    abstract override def close() = {
        super.close()
        app.getGuiViewPort.removeProcessor(niftyDisplay)
    }
}