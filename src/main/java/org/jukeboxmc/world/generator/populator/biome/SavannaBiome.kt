package org.jukeboxmc.world.generator.populator.biome

import org.jukeboxmc.world.generator.populator.SavannaTreePopulator

/**
 * @author LucGamesYT
 * @version 1.0
 */
class SavannaBiome : GrassyBiome() {
    init {
        val savannaTreePopulator = SavannaTreePopulator()
        savannaTreePopulator.setRandomAmount(2)
        addPopulator(savannaTreePopulator)
    }
}