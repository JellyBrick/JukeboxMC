package org.jukeboxmc.world.generator.noise

import java.util.Arrays
import java.util.Random
import org.jukeboxmc.world.generator.noise.bukkit.NoiseGenerator

class SimplexOctaveGenerator @JvmOverloads constructor(
    rand: Random,
    octaves: Int,
    sizeX: Int = 0,
    sizeY: Int = 0,
    sizeZ: Int = 0
) : PerlinOctaveGenerator(
    createOctaves(rand, octaves), rand, sizeX, sizeY, sizeZ
) {
    constructor(rand: Random, octaves: Int, sizeX: Int, sizeZ: Int) : this(rand, octaves, sizeX, 1, sizeZ)

    override fun getFractalBrownianMotion(
        x: Double,
        y: Double,
        z: Double,
        lacunarity: Double,
        persistence: Double
    ): DoubleArray? {
        Arrays.fill(noise, 0.0)
        var freq = 1.0
        var amp = 1.0

        // fBm
        for (octave in octaves) {
            noise = (octave as PerlinNoise).getNoise(
                noise,
                x,
                y,
                z,
                sizeX,
                sizeY,
                sizeZ,
                xScale * freq,
                yScale * freq,
                zScale * freq,
                0.55 / amp
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
                result[i] = SimplexNoise(rand)
            }
            return result
        }
    }
}