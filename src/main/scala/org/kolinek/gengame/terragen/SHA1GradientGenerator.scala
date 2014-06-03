package org.kolinek.gengame.terragen

import org.kolinek.gengame.geometry._
import java.security.MessageDigest
import java.nio.ByteBuffer

object SHA1GradientGenerator {
    def generate(seed: String): SingleCube => Position = {
        val sha = MessageDigest.getInstance("SHA-1")
        return { case Point(x, y, z) => extractPoint(sha.digest(getArray(seed, x, y, z))) }
    }

    private def getArray(s: String, x: CubeUnit, y: CubeUnit, z: CubeUnit) =
        (s ++ x.toString ++ y.toString ++ z.toString).getBytes

    private def extractPoint(b: Array[Byte]) = {
        val buf = ByteBuffer.wrap(b)
        val z = buf.getLong() ^ buf.getInt().toLong
        val p = buf.getLong()
        val phi = ((p.toDouble / Long.MaxValue.toDouble) + 1) * math.Pi
        rotToPoint(z / Long.MaxValue.toDouble, phi)
    }

    private def rotToPoint(z: Double, phi: Double) = Point(getX(z, phi).pos, getY(z, phi).pos, z.pos)

    private def getX(z: Double, phi: Double) = math.sqrt(1 - z * z) * math.cos(phi)

    private def getY(z: Double, phi: Double) = math.sqrt(1 - z * z) * math.sin(phi)
}