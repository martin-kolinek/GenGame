package org.kolinek.gengame.terragen

import org.scalatest.FunSuite
import org.kolinek.gengame.geometry._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TestSimplexNoise extends FunSuite {
    test("simplex noise zero") {
        val start = System.currentTimeMillis
        val noise = new SimplexNoise(x => Position.zero)
        val n = for (x <- -10 to 10; y <- -10 to 10; z <- -10 to 10)
            yield noise(Point(x.pos, y.pos, z.pos));
        assert(System.currentTimeMillis - start < 500)
        assert(n.sum === 0)
    }

    test("simplex noise hash") {
        val start = System.currentTimeMillis
        val noise = new SimplexNoise(SHA1GradientGenerator.generate("asdf"))
        val n = for (x <- -10 to 10; y <- -10 to 10; z <- -10 to 10)
            yield noise(Point(x.pos, y.pos, z.pos));
        assert(System.currentTimeMillis - start < 500)
        assert(math.abs(n.sum) < 50.0)
    }

}