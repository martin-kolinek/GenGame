package org.kolinek.gengame.config

import shapeless.Lens
import com.typesafe.config.Config
import net.ceedubs.ficus.Ficus._
import com.typesafe.config.ConfigValueFactory
import scala.collection.JavaConversions._
import com.jme3.system.AppSettings
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

case class AllConfig(graphics: GraphicsConfig, controls: ControlsConfig) extends ConvertableToMap

case class GraphicsConfig(width: Int, height: Int, fullscreen: Boolean) extends ConvertableToMap

case class ControlsConfig(forward: Seq[Int], back: Seq[Int],
                          left: Seq[Int], right: Seq[Int],
                          up: Seq[Int], down: Seq[Int],
                          rotleft: Seq[Int], rotright: Seq[Int]) extends ConvertableToMap

object Lenses {
    val allConfigLens = new Lens[Config, AllConfig] {
        def get(c: Config) = c.as[AllConfig]("gengame")
        def set(c: Config)(f: AllConfig) = c.withValue("gengame", ConfigValueFactory.fromMap(f.toMap))
    }

    val graphicsConfigLens = allConfigLens >> 'graphics

    val controlsConfigLens = allConfigLens >> 'controls
}