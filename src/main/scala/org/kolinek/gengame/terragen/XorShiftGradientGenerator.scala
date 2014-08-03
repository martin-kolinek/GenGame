package org.kolinek.gengame.terragen

import org.kolinek.gengame.geometry._
import java.security.MessageDigest
import java.nio.ByteBuffer
import scala.annotation.tailrec
import scala.util.hashing.MurmurHash3

object XorShiftGradientGenerator {
    def generate(seed: String): SingleCube => Position = {
        val sha = MessageDigest.getInstance("SHA-1")
        val seedDigest = sha.digest(seed.getBytes)
        val seedLong = ByteBuffer.wrap(seedDigest).getLong
        return { case Point(x, y, z) => extractPoint(getSeed(seedLong, x, y, z)) }
    }

    private def getSeed(seed: Long, x: CubeUnit, y: CubeUnit, z: CubeUnit) = {
        val buf = ByteBuffer.allocate(32)
        buf.putLong(seed)
        buf.putLong(x.underlying)
        buf.putLong(y.underlying)
        buf.putLong(z.underlying)

        val hash = MurmurHash3.bytesHash(buf.array).toLong 
        hash << 32 |
            hash
    }

    def next(x1: Long) = {
        var x2 = x1 ^ (x1 << 21);
        x2 ^= (x2 >>> 35);
        x2 ^ (x2 << 4);
    }

    private def extractPoint(seed: Long) = {
        val z = next(seed).toInt
        val p = next(z).toInt
        val phi = ((p.toDouble / Int.MaxValue.toDouble) + 1) * math.Pi
        rotToPoint(z / Int.MaxValue.toDouble, phi)
    }

    private def rotToPoint(z: Double, phi: Double) = Point(getX(z, phi).pos, getY(z, phi).pos, z.pos)

    private def getX(z: Double, phi: Double) = math.sqrt(1 - z * z) * math.cos(phi)

    private def getY(z: Double, phi: Double) = math.sqrt(1 - z * z) * math.sin(phi)
}