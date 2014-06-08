package org.kolinek.gengame.terragen.mesh

import org.kolinek.gengame.geometry._
import scala.collection.mutable.HashMap
import spire.syntax.normedVectorSpace._

class TriangleArea(val tris: Seq[Triangle], val finished: Set[Triangle], val positions: Long => Position) {
    def this(tris: Seq[Triangle], finishedFlags: Seq[Boolean], positions: Long => Position) =
        this(tris,
            (tris zip finishedFlags).filter(_._2).map(_._1).toSet,
            positions)

    private lazy val listToTri = tris.map(x => x.toList -> x)
    private lazy val pointMap = listToTri.flatMap {
        case (l, tri) => l.map(_ -> tri)
    }.groupBy(_._1).map {
        case (pt, lst) => pt -> lst.map(_._2)
    }.toMap
    private lazy val neighMap = listToTri.map {
        case (l, tri) => tri -> l.flatMap(pointMap).filter(_ != tri)
    }.toMap
    private lazy val triPositions = listToTri.map {
        case (l, tri) => tri -> l.map(positions)
    }
    private lazy val triPositionMap = triPositions.map {
        case (tri, Seq(a, b, c)) => tri -> (a, b, c)
    }.toMap
    def neighbours(tri: Triangle) = neighMap.getOrElse(tri, Nil)

    def getTrisIn(bbox: BBoxPosition) =
        for ((tri, poses) <- triPositions if poses.exists(bbox.contains))
            yield tri

    def getTrianglePosition(tri: Triangle) = triPositionMap(tri)

    private lazy val normals = {
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

    def pointNormal(pt: Long) = normals(pt)

    def positionTri(t: Triangle) = (positions(t.a), positions(t.b), positions(t.c))

    def withFinished(fin: TrianglePatch) = new TriangleArea(tris, finished ++ fin.tris, positions)
}
