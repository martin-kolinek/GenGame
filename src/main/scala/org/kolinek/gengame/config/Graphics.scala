package org.kolinek.gengame.config

import com.jme3.system.AppSettings
import java.nio.file.Paths
import com.typesafe.config.ConfigFactory
import net.ceedubs.ficus.FicusConfig._
import rx.lang.scala.Observable
import org.kolinek.gengame.threading.AppProvider
import com.typesafe.config.Config
import com.typesafe.config.ConfigValue
import com.typesafe.config.ConfigObject
import com.typesafe.config.ConfigValueFactory
import org.kolinek.gengame.threading.ErrorHelpers
import org.kolinek.gengame.reporting.ErrorLoggingComponent

trait GraphicsConfigProvider {
    def graphicsConfig: Observable[GraphicsConfig]
}

trait DefaultGraphicsConfigProvider extends GraphicsConfigProvider {
    self: ConfigProvider =>

    lazy val graphicsConfig = config.map(Lenses.graphicsConfigLens.get)
}

trait ApplyGraphicsConfigComponent extends ErrorHelpers {
    self: GraphicsConfigProvider with AppProvider with ErrorLoggingComponent =>

    graphicsConfig.foreach { conf =>
        app.map(_.setSettings(conf.toJmeSettings))
        app.map(_.restart())
    }
}