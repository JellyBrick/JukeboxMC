package org.jukeboxmc.world.generator.noise.bukkit

/**
 * Base class for all noise generators
 */
abstract class NoiseGenerator {
    protected val perm = IntArray(512)
    protected var offsetX = 0.0
    protected var offsetY = 0.0
    protected var offsetZ = 0.0

    /**
     * Computes and returns the 1D noise for the given coordinate in 1D space
     *
     * @param x X coordinate
     * @return Noise at given location, from range -1 to 1
     */
    fun noise(x: Double): Double {
        return this.noise(x, 0.0, 0.0)
    }

    /**
     * Computes and returns the 2D noise for the given coordinates in 2D space
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return Noise at given location, from range -1 to 1
     */
    open fun noise(x: Double, y: Double): Double {
        return this.noise(x, y, 0.0)
    }

    /**
     * Computes and returns the 3D noise for the given coordinates in 3D space
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return Noise at given location, from range -1 to 1
     */
    abstract fun noise(x: Double, y: Double, z: Double): Double

    /**
     * Generates noise for the 1D coordinates using the specified number of
     * octaves and parameters
     *
     * @param x         X-coordinate
     * @param octaves   Number of octaves to use
     * @param frequency How much to alter the frequency by each octave
     * @param amplitude How much to alter the amplitude by each octave
     * @return Resulting noise
     */
    fun noise(x: Double, octaves: Int, frequency: Double, amplitude: Double): Double {
        return this.noise(x, 0.0, 0.0, octaves, frequency, amplitude)
    }

    /**
     * Generates noise for the 1D coordinates using the specified number of
     * octaves and parameters
     *
     * @param x          X-coordinate
     * @param octaves    Number of octaves to use
     * @param frequency  How much to alter the frequency by each octave
     * @param amplitude  How much to alter the amplitude by each octave
     * @param normalized If true, normalize the value to [-1, 1]
     * @return Resulting noise
     */
    fun noise(x: Double, octaves: Int, frequency: Double, amplitude: Double, normalized: Boolean): Double {
        return this.noise(x, 0.0, 0.0, octaves, frequency, amplitude, normalized)
    }

    /**
     * Generates noise for the 2D coordinates using the specified number of
     * octaves and parameters
     *
     * @param x         X-coordinate
     * @param y         Y-coordinate
     * @param octaves   Number of octaves to use
     * @param frequency How much to alter the frequency by each octave
     * @param amplitude How much to alter the amplitude by each octave
     * @return Resulting noise
     */
    fun noise(x: Double, y: Double, octaves: Int, frequency: Double, amplitude: Double): Double {
        return this.noise(x, y, 0.0, octaves, frequency, amplitude)
    }

    /**
     * Generates noise for the 2D coordinates using the specified number of
     * octaves and parameters
     *
     * @param x          X-coordinate
     * @param y          Y-coordinate
     * @param octaves    Number of octaves to use
     * @param frequency  How much to alter the frequency by each octave
     * @param amplitude  How much to alter the amplitude by each octave
     * @param normalized If true, normalize the value to [-1, 1]
     * @return Resulting noise
     */
    fun noise(x: Double, y: Double, octaves: Int, frequency: Double, amplitude: Double, normalized: Boolean): Double {
        return this.noise(x, y, 0.0, octaves, frequency, amplitude, normalized)
    }
    /**
     * Generates noise for the 3D coordinates using the specified number of
     * octaves and parameters
     *
     * @param x          X-coordinate
     * @param y          Y-coordinate
     * @param z          Z-coordinate
     * @param octaves    Number of octaves to use
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
     * @param octaves   Number of octaves to use
     * @param frequency How much to alter the frequency by each octave
     * @param amplitude How much to alter the amplitude by each octave
     * @return Resulting noise
     */
    @JvmOverloads
    fun noise(
        x: Double,
        y: Double,
        z: Double,
        octaves: Int,
        frequency: Double,
        amplitude: Double,
        normalized: Boolean = false
    ): Double {
        var result = 0.0
        var amp = 1.0
        var freq = 1.0
        var max = 0.0
        for (i in 0 until octaves) {
            result += this.noise(x * freq, y * freq, z * freq) * amp
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
        /**
         * Speedy floor, faster than (int)Math.floor(x)
         *
         * @param x Value to floor
         * @return Floored value
         */
        fun floor(x: Double): Int {
            return if (x >= 0) x.toInt() else x.toInt() - 1
        }

        protected fun fade(x: Double): Double {
            return x * x * x * (x * (x * 6 - 15) + 10)
        }

        protected fun lerp(x: Double, y: Double, z: Double): Double {
            return y + x * (z - y)
        }

        protected fun grad(hash: Int, x: Double, y: Double, z: Double): Double {
            var hash = hash
            hash = hash and 15
            val u = if (hash < 8) x else y
            val v = if (hash < 4) y else if (hash == 12 || hash == 14) x else z
            return (if (hash and 1 == 0) u else -u) + if (hash and 2 == 0) v else -v
        }
    }
}