package org.kolinek.gengame.terragen
/* Based on sdnoise1234, Simplex noise with true analytic derivative in 1D to 4D.
 * by Stefan Gustavson
 * original can be found at http://www.itn.liu.se/~stegu/aqsis/DSOs/DSOnoises.html
 */

import org.kolinek.gengame.geometry._
import spire.implicits._
import spire.std.int._

class SimplexNoise(grads: SingleCube => Position) extends (Position => Double) {
    def gradients(p: Point[Int]) = grads(p.map(_.toLong.cube)).map(_.toFloat)
    val F3 = 0.333333333f
    val G3 = 0.166666667f
    override def apply(arg: Position): Double = {
        /*v n0, n1, n2, n3; /* Noise contributions from the four simplex corners */
		float noise;          /* Return value */
		float gx0, gy0, gz0, gx1, gy1, gz1; /* Gradients at simplex corners */
		float gx2, gy2, gz2, gx3, gy3, gz3;
		float x1, y1, z1, x2, y2, z2, x3, y3, z3;
		float t0, t1, t2, t3, t20, t40, t21, t41, t22, t42, t23, t43;
		float temp0, temp1, temp2, temp3;*/
        val x = arg.x.toFloat
        val y = arg.y.toFloat
        val z = arg.z.toFloat
        /* Skew the input space to determine which simplex cell we're in */
        val s = (x + y + z) * F3; /* Very nice and simple skew factor for 3D */
        val xs = x + s;
        val ys = y + s;
        val zs = z + s;
        val i = xs.floor.toInt;
        val j = ys.floor.toInt;
        val k = zs.floor.toInt;

        val t = (i + j + k) * G3;
        val X0 = i - t; /* Unskew the cell origin back to (x,y,z) space */
        val Y0 = j - t;
        val Z0 = k - t;
        val x0 = x - X0; /* The x,y,z distances from the cell origin */
        val y0 = y - Y0;
        val z0 = z - Z0;

        /* For the 3D case, the simplex shape is a slightly irregular tetrahedron.
		 * Determine which simplex we are in. */
        var i1, j1, k1 = 0; /* Offsets for second corner of simplex in (i,j,k) coords */
        var i2, j2, k2 = 0; /* Offsets for third corner of simplex in (i,j,k) coords */

        if (x0 >= y0) {
            if (y0 >= z0) { i1 = 1; j1 = 0; k1 = 0; i2 = 1; j2 = 1; k2 = 0; } /* X Y Z order */
            else if (x0 >= z0) { i1 = 1; j1 = 0; k1 = 0; i2 = 1; j2 = 0; k2 = 1; } /* X Z Y order */
            else { i1 = 0; j1 = 0; k1 = 1; i2 = 1; j2 = 0; k2 = 1; } /* Z X Y order */
        } else { // x0<y0
            if (y0 < z0) { i1 = 0; j1 = 0; k1 = 1; i2 = 0; j2 = 1; k2 = 1; } /* Z Y X order */
            else if (x0 < z0) { i1 = 0; j1 = 1; k1 = 0; i2 = 0; j2 = 1; k2 = 1; } /* Y Z X order */
            else { i1 = 0; j1 = 1; k1 = 0; i2 = 1; j2 = 1; k2 = 0; } /* Y X Z order */
        }

        /* A step of (1,0,0) in (i,j,k) means a step of (1-c,-c,-c) in (x,y,z),
		 * a step of (0,1,0) in (i,j,k) means a step of (-c,1-c,-c) in (x,y,z), and
		 * a step of (0,0,1) in (i,j,k) means a step of (-c,-c,1-c) in (x,y,z), where
		 * c = 1/6.   */

        val x1 = x0 - i1 + G3; /* Offsets for second corner in (x,y,z) coords */
        val y1 = y0 - j1 + G3;
        val z1 = z0 - k1 + G3;
        val x2 = x0 - i2 + 2.0f * G3; /* Offsets for third corner in (x,y,z) coords */
        val y2 = y0 - j2 + 2.0f * G3;
        val z2 = z0 - k2 + 2.0f * G3;
        val x3 = x0 - 1.0f + 3.0f * G3; /* Offsets for last corner in (x,y,z) coords */
        val y3 = y0 - 1.0f + 3.0f * G3;
        val z3 = z0 - 1.0f + 3.0f * G3;

        /* Calculate the contribution from the four corners */
        var t0 = 0.6f - x0 * x0 - y0 * y0 - z0 * z0;
        var n0, t20, t40, gx0, gy0, gz0 = 0.0f;
        if (t0 < 0.0f) t0 = 0.0f;
        else {
            val p = gradients(Point(i, j, k))
            gx0 = p.x.toFloat
            gy0 = p.y.toFloat
            gz0 = p.z.toFloat
            t20 = t0 * t0;
            t40 = t20 * t20;
            n0 = t40 * (gx0 * x0 + gy0 * y0 + gz0 * z0);
        }

        var t1 = 0.6f - x1 * x1 - y1 * y1 - z1 * z1;
        var n1, t21, t41, gx1, gy1, gz1 = 0.0f;
        if (t1 < 0.0f) t1 = 0.0f;
        else {
            val p = gradients(Point(i + i1, j + j1, k + k1))
            gx1 = p.x.toFloat
            gy1 = p.y.toFloat
            gz1 = p.z.toFloat
            t21 = t1 * t1;
            t41 = t21 * t21;
            n1 = t41 * (gx1 * x1 + gy1 * y1 + gz1 * z1);
        }

        var t2 = 0.6f - x2 * x2 - y2 * y2 - z2 * z2;
        var n2, t22, t42, gx2, gy2, gz2 = 0.0f;
        if (t2 < 0.0f) t2 = 0.0f;
        else {
            val p = gradients(Point(i + i2, j + j2, k + k2))
            gx2 = p.x.toFloat
            gy2 = p.y.toFloat
            gz2 = p.z.toFloat
            t22 = t2 * t2;
            t42 = t22 * t22;
            n2 = t42 * (gx2 * x2 + gy2 * y2 + gz2 * z2);
        }

        var t3 = 0.6f - x3 * x3 - y3 * y3 - z3 * z3;
        var n3, t23, t43, gx3, gy3, gz3 = 0.0f;
        if (t3 < 0.0f) t3 = 0.0f;
        else {
            val p = gradients(Point(i + 1, j + 1, k + 1))
            gx3 = p.x.toFloat
            gy3 = p.y.toFloat
            gz3 = p.z.toFloat
            t23 = t3 * t3;
            t43 = t23 * t23;
            n3 = t43 * (gx3 * x3 + gy3 * y3 + gz3 * z3);
        }

        /*  Add contributions from each corner to get the final noise value.
		 * The result is scaled to return values in the range [-1,1] */
        (28.0f * (n0 + n1 + n2 + n3));
    }

    def getDerivativeValue(arg: Position) = {
        val x = arg.x.toFloat
        val y = arg.y.toFloat
        val z = arg.z.toFloat
        /* Skew the input space to determine which simplex cell we're in */
        val s = (x + y + z) * F3; /* Very nice and simple skew factor for 3D */
        val xs = x + s;
        val ys = y + s;
        val zs = z + s;
        val i = xs.floor.toInt;
        val j = ys.floor.toInt;
        val k = zs.floor.toInt;

        val t = (i + j + k) * G3;
        val X0 = i - t; /* Unskew the cell origin back to (x,y,z) space */
        val Y0 = j - t;
        val Z0 = k - t;
        val x0 = x - X0; /* The x,y,z distances from the cell origin */
        val y0 = y - Y0;
        val z0 = z - Z0;

        /* For the 3D case, the simplex shape is a slightly irregular tetrahedron.
		 * Determine which simplex we are in. */
        var i1, j1, k1 = 0; /* Offsets for second corner of simplex in (i,j,k) coords */
        var i2, j2, k2 = 0; /* Offsets for third corner of simplex in (i,j,k) coords */

        if (x0 >= y0) {
            if (y0 >= z0) { i1 = 1; j1 = 0; k1 = 0; i2 = 1; j2 = 1; k2 = 0; } /* X Y Z order */
            else if (x0 >= z0) { i1 = 1; j1 = 0; k1 = 0; i2 = 1; j2 = 0; k2 = 1; } /* X Z Y order */
            else { i1 = 0; j1 = 0; k1 = 1; i2 = 1; j2 = 0; k2 = 1; } /* Z X Y order */
        } else { // x0<y0
            if (y0 < z0) { i1 = 0; j1 = 0; k1 = 1; i2 = 0; j2 = 1; k2 = 1; } /* Z Y X order */
            else if (x0 < z0) { i1 = 0; j1 = 1; k1 = 0; i2 = 0; j2 = 1; k2 = 1; } /* Y Z X order */
            else { i1 = 0; j1 = 1; k1 = 0; i2 = 1; j2 = 1; k2 = 0; } /* Y X Z order */
        }

        /* A step of (1,0,0) in (i,j,k) means a step of (1-c,-c,-c) in (x,y,z),
		 * a step of (0,1,0) in (i,j,k) means a step of (-c,1-c,-c) in (x,y,z), and
		 * a step of (0,0,1) in (i,j,k) means a step of (-c,-c,1-c) in (x,y,z), where
		 * c = 1/6.   */

        val x1 = x0 - i1 + G3; /* Offsets for second corner in (x,y,z) coords */
        val y1 = y0 - j1 + G3;
        val z1 = z0 - k1 + G3;
        val x2 = x0 - i2 + 2.0f * G3; /* Offsets for third corner in (x,y,z) coords */
        val y2 = y0 - j2 + 2.0f * G3;
        val z2 = z0 - k2 + 2.0f * G3;
        val x3 = x0 - 1.0f + 3.0f * G3; /* Offsets for last corner in (x,y,z) coords */
        val y3 = y0 - 1.0f + 3.0f * G3;
        val z3 = z0 - 1.0f + 3.0f * G3;

        /* Calculate the contribution from the four corners */
        var t0 = 0.6f - x0 * x0 - y0 * y0 - z0 * z0;
        var n0, t20, t40, gx0, gy0, gz0 = 0.0f;
        if (t0 < 0.0f) t0 = 0.0f;
        else {
            val p = gradients(Point(i, j, k))
            gx0 = p.x.toFloat
            gy0 = p.y.toFloat
            gz0 = p.z.toFloat
            t20 = t0 * t0;
            t40 = t20 * t20;
        }

        var t1 = 0.6f - x1 * x1 - y1 * y1 - z1 * z1;
        var n1, t21, t41, gx1, gy1, gz1 = 0.0f;
        if (t1 < 0.0f) t1 = 0.0f;
        else {
            val p = gradients(Point(i + i1, j + j1, k + k1))
            gx1 = p.x.toFloat
            gy1 = p.y.toFloat
            gz1 = p.z.toFloat
            t21 = t1 * t1;
            t41 = t21 * t21;
        }

        var t2 = 0.6f - x2 * x2 - y2 * y2 - z2 * z2;
        var n2, t22, t42, gx2, gy2, gz2 = 0.0f;
        if (t2 < 0.0f) t2 = 0.0f;
        else {
            val p = gradients(Point(i + i2, j + j2, k + k2))
            gx2 = p.x.toFloat
            gy2 = p.y.toFloat
            gz2 = p.z.toFloat
            t22 = t2 * t2;
            t42 = t22 * t22;
        }

        var t3 = 0.6f - x3 * x3 - y3 * y3 - z3 * z3;
        var n3, t23, t43, gx3, gy3, gz3 = 0.0f;
        if (t3 < 0.0f) t3 = 0.0f;
        else {
            val p = gradients(Point(i + 1, j + 1, k + 1))
            gx3 = p.x.toFloat
            gy3 = p.y.toFloat
            gz3 = p.z.toFloat
            t23 = t3 * t3;
            t43 = t23 * t23;
        }
        val temp0 = t20 * t0 * (gx0 * x0 + gy0 * y0 + gz0 * z0);
        var dnoise_dx = temp0 * x0;
        var dnoise_dy = temp0 * y0;
        var dnoise_dz = temp0 * z0;
        val temp1 = t21 * t1 * (gx1 * x1 + gy1 * y1 + gz1 * z1);
        dnoise_dx += temp1 * x1;
        dnoise_dy += temp1 * y1;
        dnoise_dz += temp1 * z1;
        val temp2 = t22 * t2 * (gx2 * x2 + gy2 * y2 + gz2 * z2);
        dnoise_dx += temp2 * x2;
        dnoise_dy += temp2 * y2;
        dnoise_dz += temp2 * z2;
        val temp3 = t23 * t3 * (gx3 * x3 + gy3 * y3 + gz3 * z3);
        dnoise_dx += temp3 * x3;
        dnoise_dy += temp3 * y3;
        dnoise_dz += temp3 * z3;
        dnoise_dx *= -8.0f;
        dnoise_dy *= -8.0f;
        dnoise_dz *= -8.0f;
        dnoise_dx += t40 * gx0 + t41 * gx1 + t42 * gx2 + t43 * gx3;
        dnoise_dy += t40 * gy0 + t41 * gy1 + t42 * gy2 + t43 * gy3;
        dnoise_dz += t40 * gz0 + t41 * gz1 + t42 * gz2 + t43 * gz3;
        dnoise_dx *= 28.0f; /* Scale derivative to match the noise scaling */
        dnoise_dy *= 28.0f;
        dnoise_dz *= 28.0f;

        Point(dnoise_dx.toDouble.pos, dnoise_dy.toDouble.pos, dnoise_dz.toDouble.pos)
    }
}
