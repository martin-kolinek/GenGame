package org.kolinek.gengame.terragen.marchingcubes

import org.scalatest.FunSuite

class TestMarchCubeCases extends FunSuite {
    test("MarchCubeCases has all cases") {
        assert(MarchCubeCases.getAllMarchingCubeCases.size === 256)
    }
}