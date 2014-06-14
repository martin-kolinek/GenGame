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
import org.kolinek.gengame.game.nifty.GameNiftyProvider
import org.kolinek.gengame.reporting.DefaultFPSCounterComponent
import org.kolinek.gengame.lighting.LightingProvider
import org.kolinek.gengame.game.BasicModule
import org.kolinek.gengame.config.ConfigModule

class MainAppState extends SimpleAppState {
    class Component
        extends Closeable
        with GameAppProvider
        with BasicModule
        with FlightModule
        with ConfigModule
        with ControlsModule
        with MainNiftyModule
        with TerrainGenerationModule
        with LightingProvider

    def component = new Component
}