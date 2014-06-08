package org.kolinek.gengame.terragen.marchingcubes

import org.kolinek.gengame.geometry._
import spire.syntax.all._

class PointGrid(val size: SingleCube, f: SingleCube => Double) {
    private val stor = Array.ofDim[Double](size.x.toInt, size.y.toInt, size.z.toInt)
    for (x <- 0 until size.x.toInt; y <- 0 until size.y.toInt; z <- 0 until size.z.toInt) {
        stor(x)(y)(z) = f(Point(x.toLong.cube, y.toLong.cube, z.toLong.cube))

    }
    def getValue(pt: Point[Int]) = stor(pt.x)(pt.y)(pt.z)
    private def getOffset(cubePoint: Int) = cubePoint match {
        case 0 => Point(0.cube, 0.cube, 0.cube)
        case 1 => Point(1.cube, 0.cube, 0.cube)
        case 2 => Point(1.cube, 1.cube, 0.cube)
        case 3 => Point(0.cube, 1.cube, 0.cube)
        case 4 => Point(0.cube, 0.cube, 1.cube)
        case 5 => Point(1.cube, 0.cube, 1.cube)
        case 6 => Point(1.cube, 1.cube, 1.cube)
        case 7 => Point(0.cube, 1.cube, 1.cube)
    }
    def getValue(pt: SingleCube, cubePoint: Int): Double = {
        val of = getOffset(cubePoint)
        getValue((pt + of).map(_.toInt))
    }
}
