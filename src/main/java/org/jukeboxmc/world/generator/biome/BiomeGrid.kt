package org.jukeboxmc.world.generator.biome

import org.jukeboxmc.world.Biome

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BiomeGrid {
    val biomes = ByteArray(256)
    fun getBiome(x: Int, z: Int): Biome? {
        return Biome.Companion.findById(biomes[x or (z shl 4)].toInt() and 0xff)
    }
}