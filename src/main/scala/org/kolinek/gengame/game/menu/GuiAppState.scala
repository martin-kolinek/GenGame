package org.kolinek.gengame.game.menu

import org.kolinek.gengame.game.SimpleAppState
import com.jme3.niftygui.NiftyJmeDisplay
import org.kolinek.gengame.util.Closeable
import org.kolinek.gengame.game.nifty.GameNiftyProvider
import org.kolinek.gengame.game.UpdateStep
import org.kolinek.gengame.util.Closeable
import org.kolinek.gengame.game.AppGameControlComponent

class GuiAppState extends SimpleAppState {
    class Component
        extends GameAppProvider
        with GameNiftyProvider
        with GameMenuComponent
        with AppGameControlComponent
        with UpdateStep
        with Closeable

    def component = new Component
}