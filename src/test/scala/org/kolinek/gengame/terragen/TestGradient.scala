package org.kolinek.gengame.terragen

import org.scalatest.FunSuite
import org.kolinek.gengame.geometry._
import spire.syntax.integral._

class TestGradient extends FunSuite {

    test("SHA1GradientMapGenerator creates") {
        val f = SHA1GradientGenerator.generate("asdfa")
        val lst = (for (x <- 0.cube to 2.cube; y <- 0.cube to 2.cube)
            yield f(Point(x, y, 0.cube))).toList
        assert(lst.toString === """List(Point(0.9320383079325851.pos,0.3429077123912296.pos,0.117127679601269.pos), Point(-0.745880379239683.pos,0.35799829508148645.pos,0.5616935824664687.pos), Point(-0.9965214484698481.pos,-0.08252580945903326.pos,0.011597133813451135.pos), Point(0.5819992232360692.pos,-0.3469365910011348.pos,-0.7354671345322804.pos), Point(-0.9556451593430147.pos,-0.07425887655366467.pos,-0.2850051730710365.pos), Point(-0.6787401891119074.pos,0.5148239458633717.pos,0.5237060821204965.pos), Point(0.6951491687676333.pos,0.5811444886074344.pos,-0.4231296686866474.pos), Point(-0.09402556828419241.pos,-0.211616538592474.pos,-0.9728194247150777.pos), Point(0.2959144704095502.pos,0.2723293201045732.pos,-0.9155716070377108.pos))""")
    }

    test("SHA1 large") {
        val f = SHA1GradientGenerator.generate("adf")
        assert(f(Point(-50.cube, -50.cube, -50.cube)).toString === """Point(0.6922106970878805.pos,-0.7166739025647473.pos,0.08498745919090499.pos)""")
    }

}
