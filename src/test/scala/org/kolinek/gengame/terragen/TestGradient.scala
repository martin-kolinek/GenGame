package org.kolinek.gengame.terragen

import org.scalatest.FunSuite
import org.kolinek.gengame.geometry._
import spire.syntax.integral._

class TestGradient extends FunSuite {

    test("SHA1GradientMapGenerator creates") {
        val f = SHA1GradientGenerator.generate("asdfa")
        val lst = (for (x <- 0.cube to 2.cube; y <- 0.cube to 2.cube)
            yield f(Point(x, y, 0.cube))).toList
        assert(lst.toString === """List(Point(0.5055996406029432.pos,-0.807896241342561.pos,-0.3027749438885871.pos), Point(0.7851870456299831.pos,-0.5961932687547712.pos,-0.167436225669238.pos), Point(-0.0201028425990433.pos,-0.3136862199883798.pos,0.9493138738630337.pos), Point(0.5845641724950008.pos,-0.7200570079212103.pos,0.3739019036843611.pos), Point(-0.8553583961775806.pos,-0.42220894180289387.pos,-0.30016932479884995.pos), Point(0.3602151711248329.pos,-0.5511150670959817.pos,0.7526733775757576.pos), Point(0.07331692038788856.pos,-0.4184966411622141.pos,0.9052542132024468.pos), Point(0.6852281009338008.pos,-0.1937151168591437.pos,-0.7020946540110566.pos), Point(-0.6864961998538106.pos,0.27000869305340874.pos,-0.6751431501999166.pos))""")
    }

    test("SHA1 large") {
        val f = SHA1GradientGenerator.generate("adf")
        assert(f(Point(-50.cube, -50.cube, -50.cube)).toString === """Point(0.04649596860782296.pos,-0.2356009666979634.pos,-0.9707369929049812.pos)""")
    }

}
