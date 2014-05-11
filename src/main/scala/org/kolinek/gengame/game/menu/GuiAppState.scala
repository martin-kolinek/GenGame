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
import org.kolinek.gengame.config.ApplyGraphicsConfigComponent
import org.kolinek.gengame.config.DefaultConfigSaver
import org.kolinek.gengame.reporting.DefaultErrorLoggingComponent
import org.kolinek.gengame.game.DefaultAppProvider
import org.kolinek.gengame.game.DefaultUpdateComponent

class GuiAppState extends SimpleAppState {
    class Component
        extends GameAppProvider
        with JmeExecutionComponent
        with DefaultAppProvider
        with DefaultErrorLoggingComponent
        with GameNiftyProvider
        with DefaultConfigSaver
        with DefaultConfigProvider
        with DefaultGraphicsConfigProvider
        with GameMenuComponent
        with AppGameControlComponent
        with ApplyGraphicsConfigComponent
        with DefaultUpdateComponent
        with Closeable

    def component = new Component
}