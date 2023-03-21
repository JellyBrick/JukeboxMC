package org.jukeboxmc.world.generator.populator.biome

import org.jukeboxmc.world.generator.populator.SugarcanePopulator

/**
 * @author LucGamesYT
 * @version 1.0
 */
open class BeachBiome : BiomePopulator() {
    init {
        val sugarcanePopulator = SugarcanePopulator()
        sugarcanePopulator.setBaseAmount(0)
        sugarcanePopulator.setRandomAmount(20)
        addPopulator(sugarcanePopulator)
    }
}