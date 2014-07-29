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
        return { case Point(x, y, z) => extractPoint(MurmurHash3.bytesHash(getArray(seedDigest, x, y, z))) }
    }

    private def getArray(seed: Array[Byte], x: CubeUnit, y: CubeUnit, z: CubeUnit) = {
        val buf = ByteBuffer.allocate(24)
        //buf.put(seed)
        buf.putLong(x.underlying)
        buf.putLong(y.underlying)
        buf.putLong(z.underlying)
        buf.array
    }

    def next(x1: Long) = {
        var x2 = x1 ^ (x1 << 21);
        x2 ^= (x2 >>> 35);
        x2 ^ (x2 << 4);
    }

    private def extractPoint(seed: Int) = {
        val z = next(seed)
        val p = next(z)
        val phi = ((p.toDouble / Int.MaxValue.toDouble) + 1) * math.Pi
        rotToPoint(z / Int.MaxValue.toDouble, phi)
    }

    private def rotToPoint(z: Double, phi: Double) = Point(getX(z, phi).pos, getY(z, phi).pos, z.pos)

    private def getX(z: Double, phi: Double) = math.sqrt(1 - z * z) * math.cos(phi)

    private def getY(z: Double, phi: Double) = math.sqrt(1 - z * z) * math.sin(phi)
}