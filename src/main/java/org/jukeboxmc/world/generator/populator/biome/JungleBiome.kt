package org.jukeboxmc.world.generator.populator.biome

import org.jukeboxmc.world.generator.populator.BigJungleTreePopulator
import org.jukeboxmc.world.generator.populator.SmallJungleTreePopulator

/**
 * @author LucGamesYT
 * @version 1.0
 */
class JungleBiome : GrassyBiome() {
    init {
        val smallJungleTreePopulator = SmallJungleTreePopulator()
        smallJungleTreePopulator.setBaseAmount(10)
        addPopulator(smallJungleTreePopulator)
        val bigJungleTreePopulator = BigJungleTreePopulator()
        bigJungleTreePopulator.setBaseAmount(6)
        addPopulator(bigJungleTreePopulator)
    }
}
