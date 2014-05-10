package org.kolinek.gengame.config

import shapeless.Lens
import com.typesafe.config.Config
import net.ceedubs.ficus.FicusConfig._
import com.typesafe.config.ConfigValueFactory
import scala.collection.JavaConversions._
import com.jme3.system.AppSettings

case class AllConfig(graphics: GraphicsConfig) extends ConvertableToMap

case class GraphicsConfig(width: Int, height: Int, fullscreen: Boolean) extends ConvertableToMap {
    def toJmeSettings = {
        val settings = new AppSettings(true)
        settings.setResolution(width, height)
        settings.setFullscreen(fullscreen)
        settings
    }
}

object Lenses {
    val allConfigLens = new Lens[Config, AllConfig] {
        def get(c: Config) = c.as[AllConfig]("gengame")
        def set(c: Config)(f: AllConfig) = c.withValue("gengame", ConfigValueFactory.fromMap(f.toMap))
    }

    val graphicsConfigLens = allConfigLens >> 'graphics
}