package org.kolinek.gengame.main

import org.kolinek.gengame.game.SimpleAppState
import org.kolinek.gengame.game.UpdateStep
import org.kolinek.gengame.util.Closeable
import org.kolinek.gengame.game.DefaultUpdateComponent
import org.kolinek.gengame.game.DefaultAppProvider
import org.kolinek.gengame.game.DefaultJmeCameraComponent
import org.kolinek.gengame.threading.JmeExecutionComponent
import org.kolinek.gengame.reporting.DefaultErrorLoggingComponent
import org.kolinek.gengame.config.DefaultControlsConfigProvider
import org.kolinek.gengame.game.DefaultInputManagerProvider
import org.kolinek.gengame.config.DefaultConfigProvider
import org.kolinek.gengame.config.DefaultConfigSaver
import org.kolinek.gengame.game.DefaultAssetManager
import org.kolinek.gengame.game.DefaultSceneGraphProvider

class MainAppState extends SimpleAppState {
    class Component
        extends Closeable
        with DefaultUpdateComponent
        with JmeFlightComponent
        with DefaultFlightComponent
        with GameAppProvider
        with DefaultAppProvider
        with DefaultJmeCameraComponent
        with JmeExecutionComponent
        with DefaultErrorLoggingComponent
        with DefaultControlsComponent
        with DefaultControlsConfigProvider
        with DefaultInputManagerProvider
        with DefaultConfigProvider
        with DefaultConfigSaver
        with CubeComponent
        with DefaultAssetManager
        with DefaultSceneGraphProvider

    def component = new Component
}