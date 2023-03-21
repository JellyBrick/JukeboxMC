package org.jukeboxmc.world.generator.noise.bukkit

import java.util.Random

/**
 * Generates noise using the "classic" perlin generator
 *
 * @see SimplexNoiseGenerator "Improved" and faster version with slightly
 * different results
 */
open class PerlinNoiseGenerator : NoiseGenerator {
    protected constructor() {
        val p = intArrayOf(
            151, 160, 137, 91, 90, 15, 131, 13, 201,
            95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37,
            240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62,
            94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56,
            87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139,
            48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133,
            230, 220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25,
            63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200,
            196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3,
            64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126, 255,
            82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42,
            223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153,
            101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79,
            113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242,
            193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249,
            14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204,
            176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222,
            114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180
        )
        for (i in 0..511) {
            perm[i] = p[i and 255]
        }
    }

    /**
     * Creates a seeded perlin noise generator for the given seed
     *
     * @param seed Seed to construct this generator for
     */
    constructor(seed: Long) : this(Random(seed))
    constructor(rand: Random) {
        offsetX = rand.nextDouble() * 256
        offsetY = rand.nextDouble() * 256
        offsetZ = rand.nextDouble() * 256
        for (i in 0..255) {
            perm[i] = rand.nextInt(256)
        }
        for (i in 0..255) {
            val pos = rand.nextInt(256 - i) + i
            val old = perm[i]
            perm[i] = perm[pos]
            perm[pos] = old
            perm[i + 256] = perm[i]
        }
    }

    override fun noise(x: Double, y: Double, z: Double): Double {
        var x = x
        var y = y
        var z = z
        x += offsetX
        y += offsetY
        z += offsetZ
        val floorX: Int = NoiseGenerator.Companion.floor(x)
        val floorY: Int = NoiseGenerator.Companion.floor(y)
        val floorZ: Int = NoiseGenerator.Companion.floor(z)

        // Find unit cube containing the point
        val X = floorX and 255
        val Y = floorY and 255
        val Z = floorZ and 255

        // Get relative xyz coordinates of the point within the cube
        x -= floorX.toDouble()
        y -= floorY.toDouble()
        z -= floorZ.toDouble()

        // Compute fade curves for xyz
        val fX: Double = NoiseGenerator.Companion.fade(x)
        val fY: Double = NoiseGenerator.Companion.fade(y)
        val fZ: Double = NoiseGenerator.Companion.fade(z)

        // Hash coordinates of the cube corners
        val A = perm[X] + Y
        val AA = perm[A] + Z
        val AB = perm[A + 1] + Z
        val B = perm[X + 1] + Y
        val BA = perm[B] + Z
        val BB = perm[B + 1] + Z
        return NoiseGenerator.Companion.lerp(
            fZ, NoiseGenerator.Companion.lerp(
                fY, NoiseGenerator.Companion.lerp(
                    fX, NoiseGenerator.Companion.grad(
                        perm[AA], x, y, z
                    ), NoiseGenerator.Companion.grad(perm[BA], x - 1, y, z)
                ), NoiseGenerator.Companion.lerp(
                    fX, NoiseGenerator.Companion.grad(
                        perm[AB], x, y - 1, z
                    ), NoiseGenerator.Companion.grad(perm[BB], x - 1, y - 1, z)
                )
            ), NoiseGenerator.Companion.lerp(
                fY, NoiseGenerator.Companion.lerp(
                    fX, NoiseGenerator.Companion.grad(
                        perm[AA + 1], x, y, z - 1
                    ), NoiseGenerator.Companion.grad(perm[BA + 1], x - 1, y, z - 1)
                ), NoiseGenerator.Companion.lerp(
                    fX, NoiseGenerator.Companion.grad(
                        perm[AB + 1], x, y - 1, z - 1
                    ), NoiseGenerator.Companion.grad(perm[BB + 1], x - 1, y - 1, z - 1)
                )
            )
        )
    }

    companion object {
        protected val grad3 = arrayOf(
            intArrayOf(1, 1, 0),
            intArrayOf(-1, 1, 0),
            intArrayOf(1, -1, 0),
            intArrayOf(-1, -1, 0),
            intArrayOf(1, 0, 1),
            intArrayOf(-1, 0, 1),
            intArrayOf(1, 0, -1),
            intArrayOf(-1, 0, -1),
            intArrayOf(0, 1, 1),
            intArrayOf(0, -1, 1),
            intArrayOf(0, 1, -1),
            intArrayOf(0, -1, -1)
        )

        /**
         * Gets the singleton unseeded instance of this generator
         *
         * @return Singleton
         */
        val instance = PerlinNoiseGenerator()

        /**
         * Computes and returns the 1D unseeded perlin noise for the given
         * coordinates in 1D space
         *
         * @param x X coordinate
         * @return Noise at given location, from range -1 to 1
         */
        fun getNoise(x: Double): Double {
            return instance.noise(x)
        }

        /**
         * Computes and returns the 2D unseeded perlin noise for the given
         * coordinates in 2D space
         *
         * @param x X coordinate
         * @param y Y coordinate
         * @return Noise at given location, from range -1 to 1
         */
        fun getNoise(x: Double, y: Double): Double {
            return instance.noise(x, y)
        }

        /**
         * Computes and returns the 3D unseeded perlin noise for the given
         * coordinates in 3D space
         *
         * @param x X coordinate
         * @param y Y coordinate
         * @param z Z coordinate
         * @return Noise at given location, from range -1 to 1
         */
        fun getNoise(x: Double, y: Double, z: Double): Double {
            return instance.noise(x, y, z)
        }

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
        fun getNoise(x: Double, octaves: Int, frequency: Double, amplitude: Double): Double {
            return instance.noise(x, octaves, frequency, amplitude)
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
        fun getNoise(x: Double, y: Double, octaves: Int, frequency: Double, amplitude: Double): Double {
            return instance.noise(x, y, octaves, frequency, amplitude)
        }

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
        fun getNoise(x: Double, y: Double, z: Double, octaves: Int, frequency: Double, amplitude: Double): Double {
            return instance.noise(x, y, z, octaves, frequency, amplitude)
        }
    }
}