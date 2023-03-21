package org.jukeboxmc.world.generator.populator.biome

/**
 * @author LucGamesYT
 * @version 1.0
 */
class OceanBiome : BiomePopulator() {
    init {
        val kelpPopulator = KelpPopulator()
        kelpPopulator.setBaseAmount(-135)
        kelpPopulator.setRandomAmount(180)
        addPopulator(kelpPopulator)
        val seagrassPopulator = SeagrassPopulator(0.3)
        seagrassPopulator.setBaseAmount(24)
        seagrassPopulator.setRandomAmount(24)
        addPopulator(seagrassPopulator)
    }
}