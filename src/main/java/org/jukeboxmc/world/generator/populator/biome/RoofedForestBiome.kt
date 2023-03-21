package org.jukeboxmc.world.generator.populator.biome

import org.jukeboxmc.world.generator.populator.DarkOakTreePopulator

/**
 * @author LucGamesYT
 * @version 1.0
 */
class RoofedForestBiome : GrassyBiome() {
    init {
        val darkOakTreePopulator = DarkOakTreePopulator()
        darkOakTreePopulator.setBaseAmount(20)
        darkOakTreePopulator.setRandomAmount(10)
        addPopulator(darkOakTreePopulator)
    }
}