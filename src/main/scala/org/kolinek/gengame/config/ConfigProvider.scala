package org.kolinek.gengame.config

import com.typesafe.config.Config
import java.nio.file.Paths
import com.typesafe.config.ConfigFactory

trait ConfigProvider {
    def config: Config
}

trait DefaultConfigProvider extends ConfigProvider {
    lazy val path = Paths.get(System.getProperty("user.home"), ".GenGame", "config.conf")

    lazy val config = ConfigFactory.systemProperties().
        withFallback(ConfigFactory.parseFileAnySyntax(path.toFile())).
        withFallback(ConfigFactory.load())
}