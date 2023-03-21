package org.jukeboxmc.world.generator.noise

import org.jukeboxmc.world.generator.noise.bukkit.NoiseGenerator
import org.jukeboxmc.world.generator.noise.bukkit.OctaveGenerator
import java.util.Arrays
import java.util.Random

open class PerlinOctaveGenerator(
    octaves: Array<NoiseGenerator?>,
    rand: Random?,
    val sizeX: Int,
    val sizeY: Int,
    val sizeZ: Int,
) : OctaveGenerator(octaves) {
    protected var noise: DoubleArray?

    constructor(rand: Random, octaves: Int, sizeX: Int, sizeZ: Int) : this(rand, octaves, sizeX, 1, sizeZ)
    constructor(rand: Random, octaves: Int, sizeX: Int, sizeY: Int, sizeZ: Int) : this(
        createOctaves(rand, octaves),
        rand,
        sizeX,
        sizeY,
        sizeZ,
    )

    /**
     * Creates a generator for multiple layers of Perlin noise.
     *
     * @param octaves the noise generators
     * @param rand    the PRNG
     * @param sizeX   the size on the X axis
     * @param sizeY   the size on the Y axis
     * @param sizeZ   the size on the Z axis
     */
    init {
        noise = DoubleArray(sizeX * sizeY * sizeZ)
    }

    /**
     * Generates multiple layers of noise.
     *
     * @param x           the starting X coordinate
     * @param z           the starting Z coordinate
     * @param lacunarity  layer n's frequency as a fraction of layer
     * `n - 1`'s frequency
     * @param persistence layer n's amplitude as a multiple of layer
     * `n - 1`'s amplitude
     * @return The noise array
     */
    fun getFractalBrownianMotion(x: Double, z: Double, lacunarity: Double, persistence: Double): DoubleArray? {
        return getFractalBrownianMotion(x, 0.0, z, lacunarity, persistence)
    }

    /**
     * Generates multiple layers of noise.
     *
     * @param x           the starting X coordinate
     * @param y           the starting Y coordinate
     * @param z           the starting Z coordinate
     * @param lacunarity  layer n's frequency as a fraction of layer
     * `n - 1`'s frequency
     * @param persistence layer n's amplitude as a multiple of layer
     * `n - 1`'s amplitude
     * @return The noise array
     */
    open fun getFractalBrownianMotion(
        x: Double,
        y: Double,
        z: Double,
        lacunarity: Double,
        persistence: Double,
    ): DoubleArray? {
        var x = x
        var y = y
        var z = z
        Arrays.fill(noise, 0.0)
        var freq = 1.0
        var amp = 1.0
        x *= xScale
        y *= yScale
        z *= zScale

        // fBm
        // the noise have to be periodic over x and z axis: otherwise it can go crazy with high
        // input, leading to strange oddities in terrain generation like the old minecraft farland
        // symptoms.
        for (octave in octaves) {
            var dx = x * freq
            var dz = z * freq
            // compute integer part
            var lx = floor(dx)
            var lz = floor(dz)
            // compute fractional part
            dx -= lx.toDouble()
            dz -= lz.toDouble()
            // wrap integer part to 0..16777216
            lx %= 16777216
            lz %= 16777216
            // add to fractional part
            dx += lx.toDouble()
            dz += lz.toDouble()
            val dy = y * freq
            noise = (octave as PerlinNoise).getNoise(
                noise,
                dx,
                dy,
                dz,
                sizeX,
                sizeY,
                sizeZ,
                xScale * freq,
                yScale * freq,
                zScale * freq,
                amp,
            )
            freq *= lacunarity
            amp *= persistence
        }
        return noise
    }

    companion object {
        protected fun createOctaves(rand: Random, octaves: Int): Array<NoiseGenerator?> {
            val result = arrayOfNulls<NoiseGenerator>(octaves)
            for (i in 0 until octaves) {
                result[i] = PerlinNoise(rand)
            }
            return result
        }

        protected fun floor(x: Double): Long {
            return if (x >= 0) x.toLong() else x.toLong() - 1
        }
    }
}
