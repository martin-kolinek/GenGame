package org.kolinek.gengame.terragen

import org.kolinek.gengame.geometry._
import java.security.MessageDigest
import java.nio.ByteBuffer

object SHA1GradientGenerator {
    def generate(seed: String): SingleCube => Position = {
        val sha = MessageDigest.getInstance("SHA-1")
        val seedDigest = sha.digest(seed.getBytes)
        return { case Point(x, y, z) => extractPoint(sha.digest(getArray(seedDigest, x, y, z))) }
    }

    private def getArray(seed: Array[Byte], x: CubeUnit, y: CubeUnit, z: CubeUnit) = {
        val array = Array.ofDim[Byte](64)
        val buf = ByteBuffer.allocate(64)
        buf.put(seed)
        buf.putLong(x.underlying)
        buf.putLong(y.underlying)
        buf.putLong(z.underlying)
        buf.array
    }

    private def extractPoint(b: Array[Byte]) = {
        val buf = ByteBuffer.wrap(b)
        val z = buf.getLong()
        val p = buf.getLong()
        val phi = ((p.toDouble / Long.MaxValue.toDouble) + 1) * math.Pi
        rotToPoint(z / Long.MaxValue.toDouble, phi)
    }

    private def rotToPoint(z: Double, phi: Double) = Point(getX(z, phi).pos, getY(z, phi).pos, z.pos)

    private def getX(z: Double, phi: Double) = math.sqrt(1 - z * z) * math.cos(phi)

    private def getY(z: Double, phi: Double) = math.sqrt(1 - z * z) * math.sin(phi)
}