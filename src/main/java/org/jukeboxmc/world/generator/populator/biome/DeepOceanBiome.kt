package org.jukeboxmc.world.generator.populator.biome

import org.jukeboxmc.world.generator.populator.KelpPopulator
import org.jukeboxmc.world.generator.populator.SeagrassPopulator

/**
 * @author LucGamesYT
 * @version 1.0
 */
class DeepOceanBiome : BiomePopulator() {
    init {
        val kelpPopulator = KelpPopulator()
        kelpPopulator.setBaseAmount(-135)
        kelpPopulator.setRandomAmount(180)
        addPopulator(kelpPopulator)
        val seagrassPopulator = SeagrassPopulator(0.8)
        seagrassPopulator.setBaseAmount(24)
        seagrassPopulator.setRandomAmount(24)
        addPopulator(seagrassPopulator)
    }
}