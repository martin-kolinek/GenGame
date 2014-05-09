package org.kolinek.gengame.config

import com.jme3.system.AppSettings
import java.nio.file.Paths
import com.typesafe.config.ConfigFactory
import net.ceedubs.ficus.FicusConfig._
import rx.lang.scala.Observable
import org.kolinek.gengame.game.AppProvider

case class GraphicsConfig(width: Int, height: Int, fullscreen: Boolean) {
    def toJmeSettings = {
        val settings = new AppSettings(true)
        settings.setResolution(width, height)
        settings.setFullscreen(fullscreen)
        settings
    }
}

trait GraphicsConfigProvider {
    def graphicsConfig: Observable[GraphicsConfig]
}

trait DefaultGraphicsConfigProvider extends GraphicsConfigProvider {
    self: ConfigProvider =>

    lazy val graphicsConfig = config.map(_.as[GraphicsConfig]("graphics"))
}

trait ApplyGraphicsConfigComponent {
    self: GraphicsConfigProvider with AppProvider =>

    graphicsConfig.subscribe { conf =>
        app.setSettings(conf.toJmeSettings)
        app.restart()
    }
}