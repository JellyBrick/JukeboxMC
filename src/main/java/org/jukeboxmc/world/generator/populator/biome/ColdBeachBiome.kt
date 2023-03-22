package org.jukeboxmc.world.generator.populator.biome

import org.jukeboxmc.world.generator.populator.WaterIcePopulator

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ColdBeachBiome : BeachBiome() {
    init {
        addPopulator(WaterIcePopulator())
    }
}
