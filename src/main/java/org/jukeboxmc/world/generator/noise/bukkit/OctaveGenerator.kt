package org.jukeboxmc.world.generator.noise.bukkit

/**
 * Creates noise using unbiased octaves
 */
abstract class OctaveGenerator protected constructor(octaves: Array<NoiseGenerator>) {
    private val originalOctaves = octaves
    val octaves
        /**
         * Gets a clone of the individual octaves used within this generator
         *
         * @return Clone of the individual octaves
         */
        get() = originalOctaves.clone()

    /**
     * Gets the scale used for each X-coordinates passed
     *
     * @return X scale
     */
    /**
     * Sets the scale used for each X-coordinates passed
     *
     * @param scale New X scale
     */
    var xScale = 1.0
    /**
     * Gets the scale used for each Y-coordinates passed
     *
     * @return Y scale
     */
    /**
     * Sets the scale used for each Y-coordinates passed
     *
     * @param scale New Y scale
     */
    var yScale = 1.0
    /**
     * Gets the scale used for each Z-coordinates passed
     *
     * @return Z scale
     */
    /**
     * Sets the scale used for each Z-coordinates passed
     *
     * @param scale New Z scale
     */
    var zScale = 1.0

    /**
     * Sets the scale used for all coordinates passed to this generator.
     *
     *
     * This is the equivalent to setting each coordinate to the specified
     * value.
     *
     * @param scale New value to scale each coordinate by
     */
    open fun setScale(scale: Double) {
        xScale = scale
        yScale = scale
        zScale = scale
    }

    /**
     * Generates noise for the 1D coordinates using the specified number of
     * octaves and parameters
     *
     * @param x         X-coordinate
     * @param frequency How much to alter the frequency by each octave
     * @param amplitude How much to alter the amplitude by each octave
     * @return Resulting noise
     */
    fun noise(x: Double, frequency: Double, amplitude: Double): Double {
        return this.noise(x, 0.0, 0.0, frequency, amplitude)
    }

    /**
     * Generates noise for the 1D coordinates using the specified number of
     * octaves and parameters
     *
     * @param x          X-coordinate
     * @param frequency  How much to alter the frequency by each octave
     * @param amplitude  How much to alter the amplitude by each octave
     * @param normalized If true, normalize the value to [-1, 1]
     * @return Resulting noise
     */
    fun noise(x: Double, frequency: Double, amplitude: Double, normalized: Boolean): Double {
        return this.noise(x, 0.0, 0.0, frequency, amplitude, normalized)
    }

    /**
     * Generates noise for the 2D coordinates using the specified number of
     * octaves and parameters
     *
     * @param x         X-coordinate
     * @param y         Y-coordinate
     * @param frequency How much to alter the frequency by each octave
     * @param amplitude How much to alter the amplitude by each octave
     * @return Resulting noise
     */
    fun noise(x: Double, y: Double, frequency: Double, amplitude: Double): Double {
        return this.noise(x, y, 0.0, frequency, amplitude)
    }

    /**
     * Generates noise for the 2D coordinates using the specified number of
     * octaves and parameters
     *
     * @param x          X-coordinate
     * @param y          Y-coordinate
     * @param frequency  How much to alter the frequency by each octave
     * @param amplitude  How much to alter the amplitude by each octave
     * @param normalized If true, normalize the value to [-1, 1]
     * @return Resulting noise
     */
    fun noise(x: Double, y: Double, frequency: Double, amplitude: Double, normalized: Boolean): Double {
        return this.noise(x, y, 0.0, frequency, amplitude, normalized)
    }


    /**
     * Generates noise for the 3D coordinates using the specified number of
     * octaves and parameters
     *
     * @param x          X-coordinate
     * @param y          Y-coordinate
     * @param z          Z-coordinate
     * @param frequency  How much to alter the frequency by each octave
     * @param amplitude  How much to alter the amplitude by each octave
     * @param normalized If true, normalize the value to [-1, 1]
     * @return Resulting noise
     */
    /**
     * Generates noise for the 3D coordinates using the specified number of
     * octaves and parameters
     *
     * @param x         X-coordinate
     * @param y         Y-coordinate
     * @param z         Z-coordinate
     * @param frequency How much to alter the frequency by each octave
     * @param amplitude How much to alter the amplitude by each octave
     * @return Resulting noise
     */
    @JvmOverloads
    fun noise(
        x: Double,
        y: Double,
        z: Double,
        frequency: Double,
        amplitude: Double,
        normalized: Boolean = false,
    ): Double {
        var x = x
        var y = y
        var z = z
        var result = 0.0
        var amp = 1.0
        var freq = 1.0
        var max = 0.0
        x *= xScale
        y *= yScale
        z *= zScale
        for (octave in octaves) {
            result += octave!!.noise(x * freq, y * freq, z * freq) * amp
            max += amp
            freq *= frequency
            amp *= amplitude
        }
        if (normalized) {
            result /= max
        }
        return result
    }
}
