package org.kolinek.gengame.config

import java.nio.file.Paths
import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config
import org.apache.commons.io.FileUtils

trait ConfigSaver {
    def configFromFile: Config
}

trait DefaultConfigSaver extends ConfigSaver {
    self: ConfigProvider =>

    private def path = Paths.get(System.getProperty("user.home"), ".GenGame", "config.conf")

    def configFromFile = ConfigFactory.parseFileAnySyntax(path.toFile()).
        withFallback(ConfigFactory.load())

    config.foreach { cfg =>
        val str = cfg.withOnlyPath("gengame").root().render()
        FileUtils.writeStringToFile(path.toFile, str)
    }

}