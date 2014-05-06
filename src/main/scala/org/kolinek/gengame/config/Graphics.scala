package org.kolinek.gengame.config

import com.jme3.system.AppSettings
import java.nio.file.Paths
import com.typesafe.config.ConfigFactory
import net.ceedubs.ficus.FicusConfig._

case class GraphicsConfig(width: Int, height: Int, fullscreen: Boolean) {
    def toJmeSettings = {
        val settings = new AppSettings(true)
        settings.setResolution(width, height)
        settings.setFullscreen(fullscreen)
        settings
    }
}

trait GraphicsConfigProvider {
    def graphicsConfig: GraphicsConfig
}

trait DefaultGraphicsConfigProvider extends GraphicsConfigProvider {
    self: ConfigProvider =>

    lazy val graphicsConfig = config.as[GraphicsConfig]("graphics")
}

trait GraphicsConfigSaver {
    def saveGraphicsConfig(c: GraphicsConfig)
}