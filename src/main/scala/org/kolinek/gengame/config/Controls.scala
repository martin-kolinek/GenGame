package org.kolinek.gengame.config

import rx.lang.scala.Observable

trait ControlsConfigProvider {
    def controlsConfig: Observable[ControlsConfig]
}

trait DefaultControlsConfigProvider extends ControlsConfigProvider {
    self: ConfigProvider =>
    lazy val controlsConfig = config.map(Lenses.controlsConfigLens.get)
}