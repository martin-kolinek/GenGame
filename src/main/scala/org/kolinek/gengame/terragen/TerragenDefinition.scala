package org.kolinek.gengame.terragen

import org.kolinek.gengame.geometry._
import spire.syntax.all._

case class TerragenDefinition(func: SingleCube => Double, treshold: Double)

trait TerragenDefinitionProvider {
    def terragenDefinition: TerragenDefinition
}

trait DefaultTerragenDefinitionProvider extends TerragenDefinitionProvider {
    lazy val terragenDefinition = {
        //val func = new SimplexNoise(SHA1GradientGenerator.generate("seed"))

        val func: Position => Double = p => math.sin(p.x.toDouble)
        TerragenDefinition(p => func(p.lower), 0.0)
    }
}