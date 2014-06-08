package org.kolinek.gengame.terragen.marchingcubes

import org.kolinek.gengame.geometry._
import spire.syntax.all._

class EdgeGrid(pg: PointGrid, treshold: Double) {
    private val (sx, sy, sz) = (pg.size.x, pg.size.y, pg.size.z)
    private val storx = Array.ofDim[Double](pg.size.x.toInt - 1, pg.size.y.toInt, pg.size.z.toInt)
    private val story = Array.ofDim[Double](pg.size.x.toInt, pg.size.y.toInt - 1, pg.size.z.toInt)
    private val storz = Array.ofDim[Double](pg.size.x.toInt, pg.size.y.toInt, pg.size.z.toInt - 1)

    private def compValue(val1: Double, val2: Double, treshold: Double): Double = {
        if (val1 > val2)
            1 - compValue(val2, val1, treshold)
        else if (val1 > treshold || val2 < treshold)
            -1
        else
            (treshold - val1) / (val2 - val1)
    }

    for (x <- 0 to sx.toInt - 1; y <- 0 to sy.toInt - 1; z <- 0 to sz.toInt - 1) {
        if (x < sx - 1)
            storx(x)(y)(z) = compValue(pg.getValue(Point(x, y, z)), pg.getValue(Point(x + 1, y, z)), treshold)
        if (y < sy - 1)
            story(x)(y)(z) = compValue(pg.getValue(Point(x, y, z)), pg.getValue(Point(x, y + 1, z)), treshold)
        if (z < sz - 1)
            storz(x)(y)(z) = compValue(pg.getValue(Point(x, y, z)), pg.getValue(Point(x, y, z + 1)), treshold)
    }

    private def getStor(edge: Int) = edge match {
        case x if x <= 7 && x >= 4 => storz
        case x if x % 2 == 0 => storx
        case _ => story
    }

    private def getOffset(edge: Int) = edge match {
        case 0 => Point(0.cube, 0.cube, 0.cube)
        case 1 => Point(1.cube, 0.cube, 0.cube)
        case 2 => Point(0.cube, 1.cube, 0.cube)
        case 3 => Point(0.cube, 0.cube, 0.cube)
        case 4 => Point(0.cube, 0.cube, 0.cube)
        case 5 => Point(1.cube, 0.cube, 0.cube)
        case 6 => Point(1.cube, 1.cube, 0.cube)
        case 7 => Point(0.cube, 1.cube, 0.cube)
        case 8 => Point(0.cube, 0.cube, 1.cube)
        case 9 => Point(1.cube, 0.cube, 1.cube)
        case 10 => Point(0.cube, 1.cube, 1.cube)
        case 11 => Point(0.cube, 0.cube, 1.cube)
        case _ => throw new IllegalArgumentException("No such edge" + edge.toString)
    }

    private def constructPoint(pt: SingleCube, add: PositionUnit, edge: Int): Position = {
        val addPt = edge match {
            case e if e <= 7 && e >= 4 => Point(0.pos, 0.pos, add)
            case e if e % 2 == 0 => Point(add, 0.pos, 0.pos)
            case _ => Point(0.pos, add, 0.pos)
        }
        val x = pt.lower
        x + addPt
    }

    def getPoint(pt: SingleCube, edg: Int): Position = {
        val off = getOffset(edg)
        val opt = pt + off
        val st = getStor(edg)
        constructPoint(opt, st(opt.x.toInt)(opt.y.toInt)(opt.z.toInt).pos, edg)
    }
}
