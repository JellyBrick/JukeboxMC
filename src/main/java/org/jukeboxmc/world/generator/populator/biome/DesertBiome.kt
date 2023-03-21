package org.jukeboxmc.world.generator.populator.biome

/**
 * @author LucGamesYT
 * @version 1.0
 */
class DesertBiome : BiomePopulator() {
    init {
        val cactusPopulator = CactusPopulator()
        cactusPopulator.setBaseAmount(2)
        addPopulator(cactusPopulator)
        val deadBushPopulator = DeadBushPopulator()
        deadBushPopulator.setBaseAmount(2)
        addPopulator(deadBushPopulator)
    }
}