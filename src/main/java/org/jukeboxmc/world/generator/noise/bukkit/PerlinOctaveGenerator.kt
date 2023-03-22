package org.jukeboxmc.world.generator.noise.bukkit

import java.util.Random

/**
 * Creates perlin noise through unbiased octaves
 */
class PerlinOctaveGenerator(rand: Random, octaves: Int) : OctaveGenerator(createOctaves(rand, octaves)) {
    constructor(seed: Long, octaves: Int) : this(Random(seed), octaves)

    companion object {
        private fun createOctaves(rand: Random, octaves: Int): Array<NoiseGenerator> {
            return Array(octaves) {
                PerlinNoiseGenerator(rand)
            }
        }
    }
}
