package org.jukeboxmc.world.generator.populator.biome

import org.jukeboxmc.world.generator.populator.TallGrassPopulator

/**
 * @author LucGamesYT
 * @version 1.0
 */
open class GrassyBiome : BiomePopulator() {
    init {
        val tallGrassPopulator = TallGrassPopulator()
        tallGrassPopulator.setBaseAmount(30)
        addPopulator(tallGrassPopulator)
    }
}