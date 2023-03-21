package org.jukeboxmc.world.generator.noise

import java.util.Random

/**
 * A speed-improved simplex noise algorithm.
 *
 *
 *
 * Based on example code by Stefan Gustavson (stegu@itn.liu.se). Optimisations
 * by Peter Eastman (peastman@drizzle.stanford.edu). Better rank ordering method
 * by Stefan Gustavson in 2012.
 *
 *
 *
 * This could be sped up even further, but it's useful as is.
 */
class SimplexNoise(rand: Random) : PerlinNoise(rand) {
    protected val permMod12 = IntArray(512)

    /**
     * Creates a simplex noise generator.
     *
     * @param rand the PRNG to use
     */
    init {
        for (i in 0..511) {
            permMod12[i] = perm[i] % 12
        }
    }

    override fun get2dNoise(
        noise: DoubleArray?,
        x: Double,
        z: Double,
        sizeX: Int,
        sizeY: Int,
        scaleX: Double,
        scaleY: Double,
        amplitude: Double
    ): DoubleArray? {
        var index = 0
        for (i in 0 until sizeY) {
            val zin = offsetY + (z + i) * scaleY
            for (j in 0 until sizeX) {
                val xin = offsetX + (x + j) * scaleX
                noise!![index++] += simplex2D(xin, zin) * amplitude
            }
        }
        return noise
    }

    override fun get3dNoise(
        noise: DoubleArray?,
        x: Double,
        y: Double,
        z: Double,
        sizeX: Int,
        sizeY: Int,
        sizeZ: Int,
        scaleX: Double,
        scaleY: Double,
        scaleZ: Double,
        amplitude: Double
    ): DoubleArray? {
        var index = 0
        for (i in 0 until sizeZ) {
            val zin = offsetZ + (z + i) * scaleZ
            for (j in 0 until sizeX) {
                val xin = offsetX + (x + j) * scaleX
                for (k in 0 until sizeY) {
                    val yin = offsetY + (y + k) * scaleY
                    noise!![index++] += simplex3D(xin, yin, zin) * amplitude
                }
            }
        }
        return noise
    }

    override fun noise(xin: Double, yin: Double): Double {
        var xin = xin
        var yin = yin
        xin += offsetX
        yin += offsetY
        return simplex2D(xin, yin)
    }

    override fun noise(xin: Double, yin: Double, zin: Double): Double {
        var xin = xin
        var yin = yin
        var zin = zin
        xin += offsetX
        yin += offsetY
        zin += offsetZ
        return simplex3D(xin, yin, zin)
    }

    private fun simplex2D(xin: Double, yin: Double): Double {
        // Skew the input space to determine which simplex cell we're in
        val s = (xin + yin) * F2 // Hairy factor for 2D
        val i = floor(xin + s)
        val j = floor(yin + s)
        val t = (i + j) * G2
        val dx0 = i - t // Unskew the cell origin back to (x,y) space
        val dy0 = j - t
        val x0 = xin - dx0 // The x,y distances from the cell origin
        val y0 = yin - dy0

        // For the 2D case, the simplex shape is an equilateral triangle.
        // Determine which simplex we are in.
        val i1: Int // Offsets for second (middle) corner of simplex in (i,j) coords
        val j1: Int
        if (x0 > y0) {
            i1 = 1 // lower triangle, XY order: (0,0)->(1,0)->(1,1)
            j1 = 0
        } else {
            i1 = 0 // upper triangle, YX order: (0,0)->(0,1)->(1,1)
            j1 = 1
        }

        // A step of (1,0) in (i,j) means a step of (1-c,-c) in (x,y), and
        // a step of (0,1) in (i,j) means a step of (-c,1-c) in (x,y), where
        // c = (3-sqrt(3))/6
        val x1 = x0 - i1 + G2 // Offsets for middle corner in (x,y) unskewed coords
        val y1 = y0 - j1 + G2
        val x2 = x0 + G22 // Offsets for last corner in (x,y) unskewed coords
        val y2 = y0 + G22

        // Work out the hashed gradient indices of the three simplex corners
        val ii = i and 255
        val jj = j and 255
        val gi0 = permMod12[ii + perm[jj]]
        val gi1 = permMod12[ii + i1 + perm[jj + j1]]
        val gi2 = permMod12[ii + 1 + perm[jj + 1]]

        // Calculate the contribution from the three corners
        var t0 = 0.5 - x0 * x0 - y0 * y0
        val n0: Double
        if (t0 < 0) {
            n0 = 0.0
        } else {
            t0 *= t0
            n0 = t0 * t0 * dot(grad3[gi0], x0, y0) // (x,y) of grad3 used for 2D gradient
        }
        var t1 = 0.5 - x1 * x1 - y1 * y1
        val n1: Double
        if (t1 < 0) {
            n1 = 0.0
        } else {
            t1 *= t1
            n1 = t1 * t1 * dot(grad3[gi1], x1, y1)
        }
        var t2 = 0.5 - x2 * x2 - y2 * y2
        val n2: Double
        if (t2 < 0) {
            n2 = 0.0
        } else {
            t2 *= t2
            n2 = t2 * t2 * dot(grad3[gi2], x2, y2)
        }

        // Add contributions from each corner to get the final noise value.
        // The result is scaled to return values in the interval [-1,1].
        return 70.0 * (n0 + n1 + n2)
    }

    private fun simplex3D(xin: Double, yin: Double, zin: Double): Double {
        // Skew the input space to determine which simplex cell we're in
        val s = (xin + yin + zin) * F3 // Very nice and simple skew factor for 3D
        val i = floor(xin + s)
        val j = floor(yin + s)
        val k = floor(zin + s)
        val t = (i + j + k) * G3
        val dx0 = i - t // Unskew the cell origin back to (x,y,z) space
        val dy0 = j - t
        val dz0 = k - t

        // For the 3D case, the simplex shape is a slightly irregular tetrahedron.
        val i1: Int // Offsets for second corner of simplex in (i,j,k) coords
        val j1: Int
        val k1: Int
        val i2: Int // Offsets for third corner of simplex in (i,j,k) coords
        val j2: Int
        val k2: Int
        val x0 = xin - dx0 // The x,y,z distances from the cell origin
        val y0 = yin - dy0
        val z0 = zin - dz0
        // Determine which simplex we are in
        if (x0 >= y0) {
            if (y0 >= z0) {
                i1 = 1 // X Y Z order
                j1 = 0
                k1 = 0
                i2 = 1
                j2 = 1
                k2 = 0
            } else if (x0 >= z0) {
                i1 = 1 // X Z Y order
                j1 = 0
                k1 = 0
                i2 = 1
                j2 = 0
                k2 = 1
            } else {
                i1 = 0 // Z X Y order
                j1 = 0
                k1 = 1
                i2 = 1
                j2 = 0
                k2 = 1
            }
        } else { // x0<y0
            if (y0 < z0) {
                i1 = 0 // Z Y X order
                j1 = 0
                k1 = 1
                i2 = 0
                j2 = 1
                k2 = 1
            } else if (x0 < z0) {
                i1 = 0 // Y Z X order
                j1 = 1
                k1 = 0
                i2 = 0
                j2 = 1
                k2 = 1
            } else {
                i1 = 0 // Y X Z order
                j1 = 1
                k1 = 0
                i2 = 1
                j2 = 1
                k2 = 0
            }
        }

        // A step of (1,0,0) in (i,j,k) means a step of (1-c,-c,-c) in (x,y,z),
        // a step of (0,1,0) in (i,j,k) means a step of (-c,1-c,-c) in (x,y,z), and
        // a step of (0,0,1) in (i,j,k) means a step of (-c,-c,1-c) in (x,y,z), where
        // c = 1/6.
        val x1 = x0 - i1 + G3 // Offsets for second corner in (x,y,z) coords
        val y1 = y0 - j1 + G3
        val z1 = z0 - k1 + G3
        val x2 = x0 - i2 + G32 // Offsets for third corner in (x,y,z) coords
        val y2 = y0 - j2 + G32
        val z2 = z0 - k2 + G32

        // Work out the hashed gradient indices of the four simplex corners
        val ii = i and 255
        val jj = j and 255
        val kk = k and 255
        val gi0 = permMod12[ii + perm[jj + perm[kk]]]
        val gi1 = permMod12[ii + i1 + perm[jj + j1 + perm[kk + k1]]]
        val gi2 = permMod12[ii + i2 + perm[jj + j2 + perm[kk + k2]]]
        val gi3 = permMod12[ii + 1 + perm[jj + 1 + perm[kk + 1]]]

        // Calculate the contribution from the four corners
        var t0 = 0.5 - x0 * x0 - y0 * y0 - z0 * z0
        val n0: Double // Noise contributions from the four corners
        if (t0 < 0) {
            n0 = 0.0
        } else {
            t0 *= t0
            n0 = t0 * t0 * dot(grad3[gi0], x0, y0, z0)
        }
        var t1 = 0.5 - x1 * x1 - y1 * y1 - z1 * z1
        val n1: Double
        if (t1 < 0) {
            n1 = 0.0
        } else {
            t1 *= t1
            n1 = t1 * t1 * dot(grad3[gi1], x1, y1, z1)
        }
        var t2 = 0.5 - x2 * x2 - y2 * y2 - z2 * z2
        val n2: Double
        if (t2 < 0) {
            n2 = 0.0
        } else {
            t2 *= t2
            n2 = t2 * t2 * dot(grad3[gi2], x2, y2, z2)
        }
        val x3 = x0 + G33 // Offsets for last corner in (x,y,z) coords
        val y3 = y0 + G33
        val z3 = z0 + G33
        var t3 = 0.5 - x3 * x3 - y3 * y3 - z3 * z3
        val n3: Double
        if (t3 < 0) {
            n3 = 0.0
        } else {
            t3 *= t3
            n3 = t3 * t3 * dot(grad3[gi3], x3, y3, z3)
        }

        // Add contributions from each corner to get the final noise value.
        // The result is scaled to stay just inside [-1,1]
        return 32.0 * (n0 + n1 + n2 + n3)
    }

    // Inner class to speed up gradient computations
    // (array access is a lot slower than member access)
    class Grad internal constructor(var x: Double, var y: Double, var z: Double)
    companion object {
        protected const val SQRT_3 = 1.7320508075688772 // Math.sqrt(3)
        protected const val F2 = 0.5 * (SQRT_3 - 1)
        protected const val G2 = (3 - SQRT_3) / 6
        protected const val G22 = G2 * 2.0 - 1
        protected const val F3 = 1.0 / 3.0
        protected const val G3 = 1.0 / 6.0
        protected const val G32 = G3 * 2.0
        protected const val G33 = G3 * 3.0 - 1.0
        private val grad3 = arrayOf(
            Grad(1.0, 1.0, 0.0),
            Grad(-1.0, 1.0, 0.0),
            Grad(1.0, -1.0, 0.0),
            Grad(-1.0, -1.0, 0.0),
            Grad(1.0, 0.0, 1.0),
            Grad(-1.0, 0.0, 1.0),
            Grad(1.0, 0.0, -1.0),
            Grad(-1.0, 0.0, -1.0),
            Grad(0.0, 1.0, 1.0),
            Grad(0.0, -1.0, 1.0),
            Grad(0.0, 1.0, -1.0),
            Grad(0.0, -1.0, -1.0)
        )

        fun floor(x: Double): Int {
            return if (x > 0) x.toInt() else x.toInt() - 1
        }

        protected fun dot(g: Grad, x: Double, y: Double): Double {
            return g.x * x + g.y * y
        }

        protected fun dot(g: Grad, x: Double, y: Double, z: Double): Double {
            return g.x * x + g.y * y + g.z * z
        }
    }
}