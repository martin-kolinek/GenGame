package org.kolinek.gengame.terragen.mesh

import org.kolinek.gengame.geometry._
import scala.collection.mutable.HashMap
import spire.syntax.normedVectorSpace._

class TriangleAreaUtils(tris: Seq[Triangle], positions: Int => Position) {
    private lazy val listToTri = tris.map(x => x.toList -> x)
    private lazy val pointMap = listToTri.flatMap {
        case (l, tri) => l.map(_ -> tri)
    }.groupBy(_._1).map {
        case (pt, lst) => pt -> lst.map(_._2)
    }.toMap
    lazy val neighMap = listToTri.map {
        case (l, tri) => tri -> l.flatMap(pointMap).filter(_ != tri)
    }.toMap
    lazy val triPositions = listToTri.map {
        case (l, tri) => tri -> l.map(positions)
    }
    lazy val triPositionMap = triPositions.map {
        case (tri, Seq(a, b, c)) => tri -> (a, b, c)
    }.toMap
    
    lazy val normals = {
        val unnorm = HashMap(pointMap.keys.map(_ -> Point(0.pos, 0.pos, 0.pos)).toSeq: _*)
        for (Triangle(a, b, c) <- tris) {
            val normal = NormalCalculator.normalForTri(positions(a), positions(b), positions(c))
            unnorm(a) += normal
            unnorm(b) += normal
            unnorm(c) += normal
        }
        unnorm.map {
            case (pt, norm) => pt -> norm.normalize
        }.toMap
    }
}

class TriangleArea private (val tris: Seq[Triangle], val positions: Int => Position, utils: TriangleAreaUtils) {

    def this(tris: Seq[Triangle], positions: Int => Position) = this(tris, positions, new TriangleAreaUtils(tris, positions))

    def neighbours(tri: Triangle) = utils.neighMap.getOrElse(tri, Nil)

    def getTrisIn(bbox: BBoxPosition) =
        for ((tri, poses) <- utils.triPositions if poses.exists(bbox.contains))
            yield tri

    def getTrianglePosition(tri: Triangle) = utils.triPositionMap(tri)

    def pointNormal(pt: Int) = utils.normals(pt)

    def positionTri(t: Triangle) = (positions(t.a), positions(t.b), positions(t.c))

    def withFinished(fin: Seq[Triangle]) = {
        val finSet = fin.toSet
        new TriangleArea(tris.filterNot(finSet.contains), positions, utils)
    }

    def empty = tris.isEmpty
}
