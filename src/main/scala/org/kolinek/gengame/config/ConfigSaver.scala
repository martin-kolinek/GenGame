package org.kolinek.gengame.config

import java.nio.file.Paths
import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config
import org.apache.commons.io.FileUtils
import org.kolinek.gengame.threading.ErrorHelpers
import org.kolinek.gengame.reporting.ErrorLoggingComponent

trait ConfigSaver {
    def configFromFile: Config
}

trait DefaultConfigSaver extends ConfigSaver with ErrorHelpers {
    self: ConfigProvider with ErrorLoggingComponent =>

    private def path = Paths.get(System.getProperty("user.home"), ".GenGame", "config.conf")

    def configFromFile = ConfigFactory.parseFileAnySyntax(path.toFile()).
        withFallback(ConfigFactory.load())

    config.foreach { cfg =>
        val str = cfg.withOnlyPath("gengame").root().render()
        FileUtils.writeStringToFile(path.toFile, str)
    }

}