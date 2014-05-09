package org.kolinek.gengame.game.menu

import org.kolinek.gengame.game.SimpleAppState
import com.jme3.niftygui.NiftyJmeDisplay
import org.kolinek.gengame.util.Closeable
import org.kolinek.gengame.game.nifty.GameNiftyProvider
import org.kolinek.gengame.game.UpdateStep
import org.kolinek.gengame.util.Closeable
import org.kolinek.gengame.game.AppGameControlComponent
import org.kolinek.gengame.config.DefaultConfigProvider
import org.kolinek.gengame.config.DefaultGraphicsConfigProvider
import org.kolinek.gengame.threading.JmeExecutionComponent

class GuiAppState extends SimpleAppState {
    class Component
        extends GameAppProvider
        with GameNiftyProvider
        with JmeExecutionComponent
        with DefaultConfigProvider
        with DefaultGraphicsConfigProvider
        with GameMenuComponent
        with AppGameControlComponent
        with UpdateStep
        with Closeable

    def component = new Component
}