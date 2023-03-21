package org.jukeboxmc.world.generator.noise.bukkit

import java.util.Random

/**
 * Creates perlin noise through unbiased octaves
 */
class PerlinOctaveGenerator(rand: Random, octaves: Int) : OctaveGenerator(createOctaves(rand, octaves)) {
    constructor(seed: Long, octaves: Int) : this(Random(seed), octaves)

    companion object {
        private fun createOctaves(rand: Random, octaves: Int): Array<NoiseGenerator?> {
            val result = arrayOfNulls<NoiseGenerator>(octaves)
            for (i in 0 until octaves) {
                result[i] = PerlinNoiseGenerator(rand)
            }
            return result
        }
    }
}
