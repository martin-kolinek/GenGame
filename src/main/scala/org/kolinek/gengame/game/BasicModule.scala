package org.kolinek.gengame.game

import org.kolinek.gengame.threading.JmeExecutionComponent
import org.kolinek.gengame.reporting.DefaultErrorLoggingComponent
import org.kolinek.gengame.main.DefaultCameraPosition
import org.kolinek.gengame.game.nifty.GameNiftyProvider
import org.kolinek.gengame.reporting.DefaultFPSCounterComponent
import org.kolinek.gengame.util.DefaultOnCloseProvider

trait BasicModule
        extends DefaultUpdateComponent
        with DefaultAppProvider
        with DefaultJmeCameraComponent
        with JmeExecutionComponent
        with DefaultErrorLoggingComponent
        with DefaultInputManagerProvider
        with DefaultAssetManager
        with DefaultSceneGraphProvider
        with DefaultCameraPosition
        with GameNiftyProvider
        with DefaultFPSCounterComponent
        with DefaultOnCloseProvider {
    self: UnsafeAppProvider =>

}