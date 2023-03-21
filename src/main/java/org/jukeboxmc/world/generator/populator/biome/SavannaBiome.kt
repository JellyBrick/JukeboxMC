package org.jukeboxmc.world.generator.populator.biome

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