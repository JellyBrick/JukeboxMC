package org.jukeboxmc.world.generator.noise.bukkit

import java.util.Random

/**
 * Creates a simplex octave generator for the given [Random]
 *
 * @param rand    Random object to construct this generator for
 * @param octaves Amount of octaves to create
 */
class SimplexOctaveGenerator(rand: Random, octaves: Int) : OctaveGenerator(createOctaves(rand, octaves)) {
    /**
     * Gets & Sets the scale used for each W-coordinates passed
     *
     * [wScale] New W scale
     * @return W scale
     */
    var wScale = 1.0

    /**
     * Creates a simplex octave generator for the given world
     *
     * @param seed    Seed to construct this generator for
     * @param octaves Amount of octaves to create
     */
    constructor(seed: Long, octaves: Int) : this(Random(seed), octaves)

    override fun setScale(scale: Double) {
        super.setScale(scale)
        wScale = scale
    }

    /**
     * Generates noise for the 3D coordinates using the specified number of
     * octaves and parameters
     *
     * @param x          X-coordinate
     * @param y          Y-coordinate
     * @param z          Z-coordinate
     * @param w          W-coordinate
     * @param frequency  How much to alter the frequency by each octave
     * @param amplitude  How much to alter the amplitude by each octave
     * @param normalized If true, normalize the value to [-1, 1]
     * @return Resulting noise
     */
    @JvmOverloads
    fun noise(
        x: Double,
        y: Double,
        z: Double,
        w: Double,
        frequency: Double,
        amplitude: Double,
        normalized: Boolean = false,
    ): Double {
        var x = x
        var y = y
        var z = z
        var w = w
        var result = 0.0
        var amp = 1.0
        var freq = 1.0
        var max = 0.0
        x *= xScale
        y *= yScale
        z *= zScale
        w *= wScale
        for (octave in octaves) {
            result += (octave as SimplexNoiseGenerator).noise(x * freq, y * freq, z * freq, w * freq) * amp
            max += amp
            freq *= frequency
            amp *= amplitude
        }
        if (normalized) {
            result /= max
        }
        return result
    }

    companion object {
        private fun createOctaves(rand: Random, octaves: Int): Array<NoiseGenerator> {
            return Array(octaves) {
                SimplexNoiseGenerator(rand)
            }
        }
    }
}
