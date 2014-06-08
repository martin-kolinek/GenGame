package org.kolinek.gengame.terragen.mesh

import org.kolinek.gengame.geometry._
import scala.collection.mutable.ArrayBuffer
import spire.syntax.normedVectorSpace._

object NormalCalculator {
    def createNormals(points: IndexedSeq[Position], indexes: Seq[Int]) = {
        val tris = indexes.grouped(3)
        val arr = new ArrayBuffer[Position](points.size)
        for (i <- 0 until points.size)
            arr += Point(0.pos, 0.pos, 0.pos)
        for (tri@Seq(x, y, z) <- tris) {
            for (a <- tri) {
                arr(a) += normalForTri(points(x), points(y), points(z))
            }
        }

        for (i <- 0 until points.size)
            arr(i) = arr(i).normalize
        arr.toList
    }

    def normalForTri(a: Position, b: Position, c: Position) = {
        val v1 = c - a
        val v2 = b - a
        cross(v1, v2)
    }

    def cross(a: Position, b: Position) = Point(a.y * b.z - a.z * b.y,
        a.z * b.x - a.x * b.z,
        a.x * b.y - a.y * b.x)
}
