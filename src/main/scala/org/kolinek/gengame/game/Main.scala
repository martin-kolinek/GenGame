package org.kolinek.gengame.game

import org.slf4j.bridge.SLF4JBridgeHandler
import com.jme3.app.SimpleApplication
import com.jme3.system.AppSettings
import com.jme3.niftygui.NiftyJmeDisplay
import com.jme3.app.DebugKeysAppState
import org.kolinek.gengame.game.menu.GuiAppState
import org.kolinek.gengame.main.MainAppState
import com.jme3.app.StatsAppState
import com.typesafe.scalalogging.slf4j.LazyLogging

class Game extends SimpleApplication(new GuiAppState, new DebugKeysAppState) with LazyLogging {

    private val isOnUpdateLoopVar = new ThreadLocal[Boolean]

    def isOnUpdateLoop = {
        isOnUpdateLoopVar.get
    }

    def simpleInitApp(): Unit = {
        isOnUpdateLoopVar.set(true)
        logger.info(s"Main thread: ${Thread.currentThread().getId()}")
    }
}

object Main extends App {
    SLF4JBridgeHandler.removeHandlersForRootLogger()
    SLF4JBridgeHandler.install()
    val g = new Game
    val settings = new AppSettings(true)
    settings.setResolution(1024, 768)
    settings.setFullscreen(false)
    g.setSettings(settings)
    g.setShowSettings(false)
    g.start()
}