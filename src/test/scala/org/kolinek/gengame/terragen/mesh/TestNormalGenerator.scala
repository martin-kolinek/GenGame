package org.kolinek.gengame.terragen.mesh

import org.scalatest.FunSuite
import org.kolinek.gengame.geometry._

class TestNormalGenerator extends FunSuite {
    test("single triangle") {
        val points = List(Point(0.pos, 0.pos, 0.pos), Point(0.pos, 0.pos, 1.pos), Point(0.pos, 1.pos, 0.pos)).toIndexedSeq
        val indexes = List(0, 1, 2)
        val normals = NormalCalculator.createNormals(points, indexes)
        assert(normals.forall(x => x == Point(1.pos, 0.pos, 0.pos)))
    }
}