package org.kolinek.gengame.terragen

import org.scalatest.FunSuite
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import spire.implicits._
import org.kolinek.gengame.geometry._
import org.kolinek.gengame.util.Timing

class TestNoisePic extends FunSuite {
    def genImage(func: SingleCube => Position, imgName: String) {
        val noise = new SimplexNoise(func)
        val noisef: (PositionUnit, PositionUnit) => Double = (x, y) => noise(Point(x, y, 0.pos))
        val img = new BufferedImage(640, 640, BufferedImage.TYPE_INT_RGB)
        val vals = for (x <- -320 to 319; y <- -320 to 319) {
            val nv = noisef(x.pos / 64.pos, y.pos / 64.0.pos)
            val cl =
                img.setRGB(x + 320, y + 320, ((nv + 1.0) * 150.0).toInt)
        }
        ImageIO.write(img, "PNG", new java.io.File(imgName))
        info(imgName)
    }

    test("Generated picture using HashGradient with SHA1") {
        info(Timing.timed {
            val hashf = SHA1GradientGenerator.generate("asdbasdf")
            genImage(hashf, "target/sha1.png")
        }.toString)
    }

    test("Generated picture using HashGradient with XorShift") {
        info(Timing.timed {
            val hashf = XorShiftGradientGenerator.generate("asdbasdf")
            genImage(hashf, "target/xorshift.png")
        }.toString)
    }

}