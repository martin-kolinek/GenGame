package org.kolinek.gengame.config

import com.typesafe.config.Config
import java.nio.file.Paths
import com.typesafe.config.ConfigFactory
import rx.lang.scala.Observable
import rx.lang.scala.subjects.BehaviorSubject

trait ConfigProvider {
    def config: Observable[Config]
}

trait ConfigSaver {
    def saveConfig(config: Config): Unit
}

trait ConfigSaverComponent {
    def configSaver: ConfigSaver
}

trait DefaultConfigProvider extends ConfigProvider with ConfigSaverComponent {

    private def path = Paths.get(System.getProperty("user.home"), ".GenGame", "config.conf")

    private def currentConfig() = {
        ConfigFactory.systemProperties().
            withFallback(ConfigFactory.parseFileAnySyntax(path.toFile())).
            withFallback(ConfigFactory.load())
    }

    lazy val config = BehaviorSubject(currentConfig())

    def configSaver = new ConfigSaver {
        def saveConfig(config: Config) {

        }
    }
}



