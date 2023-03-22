package org.jukeboxmc.world.generator.noise.bukkit

import java.util.Random
import kotlin.math.sqrt

/**
 * Generates simplex-based noise.
 *
 *
 * This is a modified version of the freely published version in the paper by
 * Stefan Gustavson at
 * [
 * http://staffwww.itn.liu.se/~stegu/simplexnoise/simplexnoise.pdf](http://staffwww.itn.liu.se/~stegu/simplexnoise/simplexnoise.pdf)
 */
open class SimplexNoiseGenerator : PerlinNoiseGenerator {
    protected var offsetW = 0.0

    protected constructor()

    /**
     * Creates a seeded simplex noise generator for the given seed
     *
     * @param seed Seed to construct this generator for
     */
    constructor(seed: Long) : this(Random(seed))
    constructor(rand: Random) : super(rand) {
        offsetW = rand.nextDouble() * 256
    }

    override fun noise(xin: Double, yin: Double, zin: Double): Double {
        var xin = xin
        var yin = yin
        var zin = zin
        xin += offsetX
        yin += offsetY
        zin += offsetZ
        val n0: Double
        val n1: Double
        val n2: Double
        val n3: Double // Noise contributions from the four corners

        // Skew the input space to determine which simplex cell we're in
        val s = (xin + yin + zin) * F3 // Very nice and simple skew factor for 3D
        val i: Int = floor(xin + s)
        val j: Int = floor(yin + s)
        val k: Int = floor(zin + s)
        val t = (i + j + k) * G3
        val X0 = i - t // Unskew the cell origin back to (x,y,z) space
        val Y0 = j - t
        val Z0 = k - t
        val x0 = xin - X0 // The x,y,z distances from the cell origin
        val y0 = yin - Y0
        val z0 = zin - Z0

        // For the 3D case, the simplex shape is a slightly irregular tetrahedron.

        // Determine which simplex we are in.
        val i1: Int
        val j1: Int
        val k1: Int // Offsets for second corner of simplex in (i,j,k) coords
        val i2: Int
        val j2: Int
        val k2: Int // Offsets for third corner of simplex in (i,j,k) coords
        if (x0 >= y0) {
            if (y0 >= z0) {
                i1 = 1
                j1 = 0
                k1 = 0
                i2 = 1
                j2 = 1
                k2 = 0
            } // X Y Z order
            else if (x0 >= z0) {
                i1 = 1
                j1 = 0
                k1 = 0
                i2 = 1
                j2 = 0
                k2 = 1
            } // X Z Y order
            else {
                i1 = 0
                j1 = 0
                k1 = 1
                i2 = 1
                j2 = 0
                k2 = 1
            } // Z X Y order
        } else { // x0<y0
            if (y0 < z0) {
                i1 = 0
                j1 = 0
                k1 = 1
                i2 = 0
                j2 = 1
                k2 = 1
            } // Z Y X order
            else if (x0 < z0) {
                i1 = 0
                j1 = 1
                k1 = 0
                i2 = 0
                j2 = 1
                k2 = 1
            } // Y Z X order
            else {
                i1 = 0
                j1 = 1
                k1 = 0
                i2 = 1
                j2 = 1
                k2 = 0
            } // Y X Z order
        }

        // A step of (1,0,0) in (i,j,k) means a step of (1-c,-c,-c) in (x,y,z),
        // a step of (0,1,0) in (i,j,k) means a step of (-c,1-c,-c) in (x,y,z), and
        // a step of (0,0,1) in (i,j,k) means a step of (-c,-c,1-c) in (x,y,z), where
        // c = 1/6.
        val x1 = x0 - i1 + G3 // Offsets for second corner in (x,y,z) coords
        val y1 = y0 - j1 + G3
        val z1 = z0 - k1 + G3
        val x2 = x0 - i2 + 2.0 * G3 // Offsets for third corner in (x,y,z) coords
        val y2 = y0 - j2 + 2.0 * G3
        val z2 = z0 - k2 + 2.0 * G3
        val x3 = x0 - 1.0 + 3.0 * G3 // Offsets for last corner in (x,y,z) coords
        val y3 = y0 - 1.0 + 3.0 * G3
        val z3 = z0 - 1.0 + 3.0 * G3

        // Work out the hashed gradient indices of the four simplex corners
        val ii = i and 255
        val jj = j and 255
        val kk = k and 255
        val gi0 = perm[ii + perm[jj + perm[kk]]] % 12
        val gi1 = perm[ii + i1 + perm[jj + j1 + perm[kk + k1]]] % 12
        val gi2 = perm[ii + i2 + perm[jj + j2 + perm[kk + k2]]] % 12
        val gi3 = perm[ii + 1 + perm[jj + 1 + perm[kk + 1]]] % 12

        // Calculate the contribution from the four corners
        var t0 = 0.6 - x0 * x0 - y0 * y0 - z0 * z0
        if (t0 < 0) {
            n0 = 0.0
        } else {
            t0 *= t0
            n0 = t0 * t0 * dot(grad3[gi0], x0, y0, z0)
        }
        var t1 = 0.6 - x1 * x1 - y1 * y1 - z1 * z1
        if (t1 < 0) {
            n1 = 0.0
        } else {
            t1 *= t1
            n1 = t1 * t1 * dot(grad3[gi1], x1, y1, z1)
        }
        var t2 = 0.6 - x2 * x2 - y2 * y2 - z2 * z2
        if (t2 < 0) {
            n2 = 0.0
        } else {
            t2 *= t2
            n2 = t2 * t2 * dot(grad3[gi2], x2, y2, z2)
        }
        var t3 = 0.6 - x3 * x3 - y3 * y3 - z3 * z3
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

    override fun noise(xin: Double, yin: Double): Double {
        var xin = xin
        var yin = yin
        xin += offsetX
        yin += offsetY
        val n0: Double
        val n1: Double
        val n2: Double // Noise contributions from the three corners

        // Skew the input space to determine which simplex cell we're in
        val s = (xin + yin) * F2 // Hairy factor for 2D
        val i: Int = floor(xin + s)
        val j: Int = floor(yin + s)
        val t = (i + j) * G2
        val X0 = i - t // Unskew the cell origin back to (x,y) space
        val Y0 = j - t
        val x0 = xin - X0 // The x,y distances from the cell origin
        val y0 = yin - Y0

        // For the 2D case, the simplex shape is an equilateral triangle.

        // Determine which simplex we are in.
        val i1: Int
        val j1: Int // Offsets for second (middle) corner of simplex in (i,j) coords
        if (x0 > y0) {
            i1 = 1
            j1 = 0
        } // lower triangle, XY order: (0,0)->(1,0)->(1,1)
        else {
            i1 = 0
            j1 = 1
        } // upper triangle, YX order: (0,0)->(0,1)->(1,1)

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
        val gi0 = perm[ii + perm[jj]] % 12
        val gi1 = perm[ii + i1 + perm[jj + j1]] % 12
        val gi2 = perm[ii + 1 + perm[jj + 1]] % 12

        // Calculate the contribution from the three corners
        var t0 = 0.5 - x0 * x0 - y0 * y0
        if (t0 < 0) {
            n0 = 0.0
        } else {
            t0 *= t0
            n0 = t0 * t0 * dot(
                grad3[gi0],
                x0,
                y0,
            ) // (x,y) of grad3 used for 2D gradient
        }
        var t1 = 0.5 - x1 * x1 - y1 * y1
        if (t1 < 0) {
            n1 = 0.0
        } else {
            t1 *= t1
            n1 = t1 * t1 * dot(grad3[gi1], x1, y1)
        }
        var t2 = 0.5 - x2 * x2 - y2 * y2
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

    /**
     * Computes and returns the 4D simplex noise for the given coordinates in
     * 4D space
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param w W coordinate
     * @return Noise at given location, from range -1 to 1
     */
    fun noise(x: Double, y: Double, z: Double, w: Double): Double {
        var x = x
        var y = y
        var z = z
        var w = w
        x += offsetX
        y += offsetY
        z += offsetZ
        w += offsetW
        val n0: Double
        val n1: Double
        val n2: Double
        val n3: Double
        val n4: Double // Noise contributions from the five corners

        // Skew the (x,y,z,w) space to determine which cell of 24 simplices we're in
        val s = (x + y + z + w) * F4 // Factor for 4D skewing
        val i: Int = floor(x + s)
        val j: Int = floor(y + s)
        val k: Int = floor(z + s)
        val l: Int = floor(w + s)
        val t = (i + j + k + l) * G4 // Factor for 4D unskewing
        val X0 = i - t // Unskew the cell origin back to (x,y,z,w) space
        val Y0 = j - t
        val Z0 = k - t
        val W0 = l - t
        val x0 = x - X0 // The x,y,z,w distances from the cell origin
        val y0 = y - Y0
        val z0 = z - Z0
        val w0 = w - W0

        // For the 4D case, the simplex is a 4D shape I won't even try to describe.
        // To find out which of the 24 possible simplices we're in, we need to
        // determine the magnitude ordering of x0, y0, z0 and w0.
        // The method below is a good way of finding the ordering of x,y,z,w and
        // then find the correct traversal order for the simplex we’re in.
        // First, six pair-wise comparisons are performed between each possible pair
        // of the four coordinates, and the results are used to add up binary bits
        // for an integer index.
        val c1 = if (x0 > y0) 32 else 0
        val c2 = if (x0 > z0) 16 else 0
        val c3 = if (y0 > z0) 8 else 0
        val c4 = if (x0 > w0) 4 else 0
        val c5 = if (y0 > w0) 2 else 0
        val c6 = if (z0 > w0) 1 else 0
        val c = c1 + c2 + c3 + c4 + c5 + c6

        // simplex[c] is a 4-vector with the numbers 0, 1, 2 and 3 in some order.
        // Many values of c will never occur, since e.g. x>y>z>w makes x<z, y<w and x<w
        // impossible. Only the 24 indices which have non-zero entries make any sense.
        // We use a thresholding to set the coordinates in turn from the largest magnitude.

        // The number 3 in the "simplex" array is at the position of the largest coordinate.
        val i1: Int = if (simplex[c][0] >= 3) 1 else 0
        val j1: Int = if (simplex[c][1] >= 3) 1 else 0
        val k1: Int = if (simplex[c][2] >= 3) 1 else 0
        val l1: Int = if (simplex[c][3] >= 3) 1 else 0 // The integer offsets for the second simplex corner

        // The number 2 in the "simplex" array is at the second-largest coordinate.
        val i2: Int = if (simplex[c][0] >= 2) 1 else 0
        val j2: Int = if (simplex[c][1] >= 2) 1 else 0
        val k2: Int = if (simplex[c][2] >= 2) 1 else 0
        val l2: Int = if (simplex[c][3] >= 2) 1 else 0 // The integer offsets for the third simplex corner

        // The number 1 in the "simplex" array is at the second-smallest coordinate.
        val i3: Int = if (simplex[c][0] >= 1) 1 else 0
        val j3: Int = if (simplex[c][1] >= 1) 1 else 0
        val k3: Int = if (simplex[c][2] >= 1) 1 else 0
        val l3: Int = if (simplex[c][3] >= 1) 1 else 0 // The integer offsets for the fourth simplex corner

        // The fifth corner has all coordinate offsets = 1, so no need to look that up.
        val x1 = x0 - i1 + G4 // Offsets for second corner in (x,y,z,w) coords
        val y1 = y0 - j1 + G4
        val z1 = z0 - k1 + G4
        val w1 = w0 - l1 + G4
        val x2 = x0 - i2 + G42 // Offsets for third corner in (x,y,z,w) coords
        val y2 = y0 - j2 + G42
        val z2 = z0 - k2 + G42
        val w2 = w0 - l2 + G42
        val x3 = x0 - i3 + G43 // Offsets for fourth corner in (x,y,z,w) coords
        val y3 = y0 - j3 + G43
        val z3 = z0 - k3 + G43
        val w3 = w0 - l3 + G43
        val x4 = x0 + G44 // Offsets for last corner in (x,y,z,w) coords
        val y4 = y0 + G44
        val z4 = z0 + G44
        val w4 = w0 + G44

        // Work out the hashed gradient indices of the five simplex corners
        val ii = i and 255
        val jj = j and 255
        val kk = k and 255
        val ll = l and 255
        val gi0 = perm[ii + perm[jj + perm[kk + perm[ll]]]] % 32
        val gi1 = perm[ii + i1 + perm[jj + j1 + perm[kk + k1 + perm[ll + l1]]]] % 32
        val gi2 = perm[ii + i2 + perm[jj + j2 + perm[kk + k2 + perm[ll + l2]]]] % 32
        val gi3 = perm[ii + i3 + perm[jj + j3 + perm[kk + k3 + perm[ll + l3]]]] % 32
        val gi4 = perm[ii + 1 + perm[jj + 1 + perm[kk + 1 + perm[ll + 1]]]] % 32

        // Calculate the contribution from the five corners
        var t0 = 0.6 - x0 * x0 - y0 * y0 - z0 * z0 - w0 * w0
        if (t0 < 0) {
            n0 = 0.0
        } else {
            t0 *= t0
            n0 = t0 * t0 * dot(grad4[gi0], x0, y0, z0, w0)
        }
        var t1 = 0.6 - x1 * x1 - y1 * y1 - z1 * z1 - w1 * w1
        if (t1 < 0) {
            n1 = 0.0
        } else {
            t1 *= t1
            n1 = t1 * t1 * dot(grad4[gi1], x1, y1, z1, w1)
        }
        var t2 = 0.6 - x2 * x2 - y2 * y2 - z2 * z2 - w2 * w2
        if (t2 < 0) {
            n2 = 0.0
        } else {
            t2 *= t2
            n2 = t2 * t2 * dot(grad4[gi2], x2, y2, z2, w2)
        }
        var t3 = 0.6 - x3 * x3 - y3 * y3 - z3 * z3 - w3 * w3
        if (t3 < 0) {
            n3 = 0.0
        } else {
            t3 *= t3
            n3 = t3 * t3 * dot(grad4[gi3], x3, y3, z3, w3)
        }
        var t4 = 0.6 - x4 * x4 - y4 * y4 - z4 * z4 - w4 * w4
        if (t4 < 0) {
            n4 = 0.0
        } else {
            t4 *= t4
            n4 = t4 * t4 * dot(grad4[gi4], x4, y4, z4, w4)
        }

        // Sum up and scale the result to cover the range [-1,1]
        return 27.0 * (n0 + n1 + n2 + n3 + n4)
    }

    companion object {
        protected val SQRT_3 = sqrt(3.0)
        protected val SQRT_5 = sqrt(5.0)
        protected val F2 = 0.5 * (SQRT_3 - 1)
        protected val G2 = (3 - SQRT_3) / 6
        protected val G22 = G2 * 2.0 - 1
        protected const val F3 = 1.0 / 3.0
        protected const val G3 = 1.0 / 6.0
        protected val F4 = (SQRT_5 - 1.0) / 4.0
        protected val G4 = (5.0 - SQRT_5) / 20.0
        protected val G42 = G4 * 2.0
        protected val G43 = G4 * 3.0
        protected val G44 = G4 * 4.0 - 1.0
        protected val grad4 = arrayOf(
            intArrayOf(0, 1, 1, 1),
            intArrayOf(0, 1, 1, -1),
            intArrayOf(0, 1, -1, 1),
            intArrayOf(0, 1, -1, -1),
            intArrayOf(0, -1, 1, 1),
            intArrayOf(0, -1, 1, -1),
            intArrayOf(0, -1, -1, 1),
            intArrayOf(0, -1, -1, -1),
            intArrayOf(1, 0, 1, 1),
            intArrayOf(1, 0, 1, -1),
            intArrayOf(1, 0, -1, 1),
            intArrayOf(1, 0, -1, -1),
            intArrayOf(-1, 0, 1, 1),
            intArrayOf(-1, 0, 1, -1),
            intArrayOf(-1, 0, -1, 1),
            intArrayOf(-1, 0, -1, -1),
            intArrayOf(1, 1, 0, 1),
            intArrayOf(1, 1, 0, -1),
            intArrayOf(1, -1, 0, 1),
            intArrayOf(1, -1, 0, -1),
            intArrayOf(-1, 1, 0, 1),
            intArrayOf(-1, 1, 0, -1),
            intArrayOf(-1, -1, 0, 1),
            intArrayOf(-1, -1, 0, -1),
            intArrayOf(1, 1, 1, 0),
            intArrayOf(1, 1, -1, 0),
            intArrayOf(1, -1, 1, 0),
            intArrayOf(1, -1, -1, 0),
            intArrayOf(-1, 1, 1, 0),
            intArrayOf(-1, 1, -1, 0),
            intArrayOf(-1, -1, 1, 0),
            intArrayOf(-1, -1, -1, 0),
        )
        protected val simplex = arrayOf(
            intArrayOf(0, 1, 2, 3),
            intArrayOf(0, 1, 3, 2),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 2, 3, 1),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(1, 2, 3, 0),
            intArrayOf(0, 2, 1, 3),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 3, 1, 2),
            intArrayOf(0, 3, 2, 1),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(1, 3, 2, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(1, 2, 0, 3),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(1, 3, 0, 2),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(2, 3, 0, 1),
            intArrayOf(2, 3, 1, 0),
            intArrayOf(1, 0, 2, 3),
            intArrayOf(1, 0, 3, 2),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(2, 0, 3, 1),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(2, 1, 3, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(2, 0, 1, 3),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(3, 0, 1, 2),
            intArrayOf(3, 0, 2, 1),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(3, 1, 2, 0),
            intArrayOf(2, 1, 0, 3),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(3, 1, 0, 2),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(3, 2, 0, 1),
            intArrayOf(3, 2, 1, 0),
        )

        /**
         * Gets the singleton unseeded instance of this generator
         *
         * @return Singleton
         */
        val instance = SimplexNoiseGenerator()
        protected fun dot(g: IntArray, x: Double, y: Double): Double {
            return g[0] * x + g[1] * y
        }

        protected fun dot(g: IntArray, x: Double, y: Double, z: Double): Double {
            return g[0] * x + g[1] * y + g[2] * z
        }

        protected fun dot(g: IntArray, x: Double, y: Double, z: Double, w: Double): Double {
            return g[0] * x + g[1] * y + g[2] * z + g[3] * w
        }

        /**
         * Computes and returns the 1D unseeded simplex noise for the given
         * coordinates in 1D space
         *
         * @param xin X coordinate
         * @return Noise at given location, from range -1 to 1
         */
        fun getNoise(xin: Double): Double {
            return instance.noise(xin)
        }

        /**
         * Computes and returns the 2D unseeded simplex noise for the given
         * coordinates in 2D space
         *
         * @param xin X coordinate
         * @param yin Y coordinate
         * @return Noise at given location, from range -1 to 1
         */
        fun getNoise(xin: Double, yin: Double): Double {
            return instance.noise(xin, yin)
        }

        /**
         * Computes and returns the 3D unseeded simplex noise for the given
         * coordinates in 3D space
         *
         * @param xin X coordinate
         * @param yin Y coordinate
         * @param zin Z coordinate
         * @return Noise at given location, from range -1 to 1
         */
        fun getNoise(xin: Double, yin: Double, zin: Double): Double {
            return instance.noise(xin, yin, zin)
        }

        /**
         * Computes and returns the 4D simplex noise for the given coordinates in
         * 4D space
         *
         * @param x X coordinate
         * @param y Y coordinate
         * @param z Z coordinate
         * @param w W coordinate
         * @return Noise at given location, from range -1 to 1
         */
        fun getNoise(x: Double, y: Double, z: Double, w: Double): Double {
            return instance.noise(x, y, z, w)
        }
    }
}
