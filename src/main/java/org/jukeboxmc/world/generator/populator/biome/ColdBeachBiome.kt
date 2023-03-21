package org.jukeboxmc.world.generator.populator.biome

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ColdBeachBiome : BeachBiome() {
    init {
        addPopulator(WaterIcePopulator())
    }
}