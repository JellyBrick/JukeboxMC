package org.jukeboxmc.world.generator.noise

import org.jukeboxmc.world.generator.noise.bukkit.PerlinNoiseGenerator
import java.util.Random

/**
 * Creates an instance using the given PRNG.
 *
 * @param rand the PRNG used to generate the seed permutation
 */
open class PerlinNoise(rand: Random) : PerlinNoiseGenerator() {
    init {
        offsetX = rand.nextDouble() * 256
        offsetY = rand.nextDouble() * 256
        offsetZ = rand.nextDouble() * 256
        // The only reason why I'm re-implementing the constructor code is that I've read
        // on at least 3 different sources that the permutation table should initially be
        // populated with indices.
        // "The permutation table is his answer to the issue of random numbers.
        // First take an array of decent length, usually 256 values. Fill it sequentially with each
        // number in that range: so index 1 gets 1, index 8 gets 8, index 251 gets 251, etc...
        // Then randomly shuffle the values, so you have a table of 256 random values, but only
        // contains the values between 0 and 255."
        // source: https://code.google.com/p/fractalterraingeneration/wiki/Perlin_Noise
        for (i in 0..255) {
            perm[i] = i
        }
        for (i in 0..255) {
            val pos = rand.nextInt(256 - i) + i
            val old = perm[i]
            perm[i] = perm[pos]
            perm[pos] = old
            perm[i + 256] = perm[i]
        }
    }

    /**
     * Generates a rectangular section of this generator's noise.
     *
     * @param noise     the output of the previous noise layer
     * @param x         the X offset
     * @param y         the Y offset
     * @param z         the Z offset
     * @param sizeX     the size on the X axis
     * @param sizeY     the size on the Y axis
     * @param sizeZ     the size on the Z axis
     * @param scaleX    the X scale parameter
     * @param scaleY    the Y scale parameter
     * @param scaleZ    the Z scale parameter
     * @param amplitude the amplitude parameter
     * @return `noise` with this layer of noise added
     */
    fun getNoise(
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
        amplitude: Double,
    ): DoubleArray? {
        return if (sizeY == 1) {
            get2dNoise(noise, x, z, sizeX, sizeZ, scaleX, scaleZ, amplitude)
        } else {
            get3dNoise(noise, x, y, z, sizeX, sizeY, sizeZ, scaleX, scaleY, scaleZ, amplitude)
        }
    }

    protected open fun get2dNoise(
        noise: DoubleArray?,
        x: Double,
        z: Double,
        sizeX: Int,
        sizeZ: Int,
        scaleX: Double,
        scaleZ: Double,
        amplitude: Double,
    ): DoubleArray? {
        var index = 0
        for (i in 0 until sizeX) {
            var dx = x + offsetX + i * scaleX
            val floorX = floor(dx)
            val ix = floorX and 255
            dx -= floorX.toDouble()
            val fx: Double = fade(dx)
            for (j in 0 until sizeZ) {
                var dz = z + offsetZ + j * scaleZ
                val floorZ = floor(dz)
                val iz = floorZ and 255
                dz -= floorZ.toDouble()
                val fz: Double = fade(dz)
                // Hash coordinates of the square corners
                val a = perm[ix]
                val aa = perm[a] + iz
                val b = perm[ix + 1]
                val ba = perm[b] + iz
                val x1: Double = lerp(
                    fx,
                    grad(perm[aa], dx, 0.0, dz),
                    grad(
                        perm[ba],
                        dx - 1,
                        0.0,
                        dz,
                    ),
                )
                val x2: Double = lerp(
                    fx,
                    grad(perm[aa + 1], dx, 0.0, dz - 1),
                    grad(
                        perm[ba + 1],
                        dx - 1,
                        0.0,
                        dz - 1,
                    ),
                )
                noise!![index++] += lerp(fz, x1, x2) * amplitude
            }
        }
        return noise
    }

    protected open fun get3dNoise(
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
        amplitude: Double,
    ): DoubleArray? {
        var n = -1
        var x1 = 0.0
        var x2 = 0.0
        var x3 = 0.0
        var x4 = 0.0
        var index = 0
        for (i in 0 until sizeX) {
            var dx = x + offsetX + i * scaleX
            val floorX = floor(dx)
            val ix = floorX and 255
            dx -= floorX.toDouble()
            val fx: Double = fade(dx)
            for (j in 0 until sizeZ) {
                var dz = z + offsetZ + j * scaleZ
                val floorZ = floor(dz)
                val iz = floorZ and 255
                dz -= floorZ.toDouble()
                val fz: Double = fade(dz)
                for (k in 0 until sizeY) {
                    var dy = y + offsetY + k * scaleY
                    val floorY = floor(dy)
                    val iy = floorY and 255
                    dy -= floorY.toDouble()
                    val fy: Double = fade(dy)
                    if (k == 0 || iy != n) {
                        n = iy
                        // Hash coordinates of the cube corners
                        val a = perm[ix] + iy
                        val aa = perm[a] + iz
                        val ab = perm[a + 1] + iz
                        val b = perm[ix + 1] + iy
                        val ba = perm[b] + iz
                        val bb = perm[b + 1] + iz
                        x1 = lerp(
                            fx,
                            grad(perm[aa], dx, dy, dz),
                            grad(
                                perm[ba],
                                dx - 1,
                                dy,
                                dz,
                            ),
                        )
                        x2 = lerp(
                            fx,
                            grad(perm[ab], dx, dy - 1, dz),
                            grad(
                                perm[bb],
                                dx - 1,
                                dy - 1,
                                dz,
                            ),
                        )
                        x3 = lerp(
                            fx,
                            grad(perm[aa + 1], dx, dy, dz - 1),
                            grad(
                                perm[ba + 1],
                                dx - 1,
                                dy,
                                dz - 1,
                            ),
                        )
                        x4 = lerp(
                            fx,
                            grad(perm[ab + 1], dx, dy - 1, dz - 1),
                            grad(
                                perm[bb + 1],
                                dx - 1,
                                dy - 1,
                                dz - 1,
                            ),
                        )
                    }
                    val y1: Double = lerp(fy, x1, x2)
                    val y2: Double = lerp(fy, x3, x4)
                    noise!![index++] += lerp(fz, y1, y2) * amplitude
                }
            }
        }
        return noise
    }

    companion object {
        fun floor(x: Double): Int {
            val floored = x.toInt()
            return if (x < floored) floored - 1 else floored
        }
    }
}
