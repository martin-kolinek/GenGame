package org.kolinek.gengame.config

import com.jme3.system.AppSettings
import java.nio.file.Paths
import com.typesafe.config.ConfigFactory
import net.ceedubs.ficus.FicusConfig._
import rx.lang.scala.Observable
import com.typesafe.config.Config
import com.typesafe.config.ConfigValue
import com.typesafe.config.ConfigObject
import com.typesafe.config.ConfigValueFactory
import org.kolinek.gengame.game.AppProvider

trait GraphicsConfigProvider {
    def graphicsConfig: Observable[GraphicsConfig]
}

trait DefaultGraphicsConfigProvider extends GraphicsConfigProvider {
    self: ConfigProvider =>

    lazy val graphicsConfig = config.map(Lenses.graphicsConfigLens.get)
}

trait ApplyGraphicsConfigComponent {
    self: GraphicsConfigProvider with AppProvider =>

    graphicsConfig.foreach { conf =>
        app.foreach { a =>
            val settings = new AppSettings(true)
            settings.setResolution(conf.width, conf.height)
            settings.setFullscreen(conf.fullscreen)
            a.setSettings(settings)
            a.restart
        }
    }
}