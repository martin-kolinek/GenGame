package org.kolinek.gengame.terragen

import org.kolinek.gengame.geometry._

case class TerragenDefinition(func: SingleCube => Double, treshold: Double)