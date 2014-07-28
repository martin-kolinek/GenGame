package org.kolinek.gengame.terragen

import org.kolinek.gengame.geometry._
import java.security.MessageDigest
import java.nio.ByteBuffer
import scala.annotation.tailrec

object XorShiftGradientGenerator {
    def generate(seed: String): SingleCube => Position = {
        val sha = MessageDigest.getInstance("SHA-1")
        val seedDigest = sha.digest(seed.getBytes)
        val buf = ByteBuffer.wrap(seedDigest)
        val seedLong = buf.getLong()
        return { case Point(x, y, z) => extractPoint(seedLong, x.underlying, y.underlying, z.underlying) }
    }

    def next(x1: Long) = {
        var x2 = x1 ^ (x1 << 21);
        x2 ^= (x2 >>> 35);
        x2 ^ (x2 << 4);
    }

    @tailrec
    def nextIt(it: Int, x: Long): Long = {
        if (it == 0)
            x
        else
            nextIt(it - 1, next(x))
    }

    private def extractPoint(stringSeed: Long, px: Long, py: Long, pz: Long) = {
        //println(s"point $px $py $pz")
        val seed = stringSeed ^ nextIt(6, px) ^ nextIt(8, py) ^ nextIt(13, pz)
        val z = next(seed)
        val p = next(z)
        //println(s"seed $seed")
        //println(s"z $z")
        //println(s"p $p")
        val phi = ((p.toDouble / Long.MaxValue.toDouble) + 1) * math.Pi
        //println(s"phi $phi")
        val zz = z / Long.MaxValue.toDouble
        //println(s"zz $zz")
        rotToPoint(zz, phi)
    }

    private def rotToPoint(z: Double, phi: Double) = Point(getX(z, phi).pos, getY(z, phi).pos, z.pos)

    private def getX(z: Double, phi: Double) = math.sqrt(1 - z * z) * math.cos(phi)

    private def getY(z: Double, phi: Double) = math.sqrt(1 - z * z) * math.sin(phi)
}