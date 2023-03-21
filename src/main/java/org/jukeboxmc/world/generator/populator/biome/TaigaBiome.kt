package org.jukeboxmc.world.generator.populator.biome

import org.jukeboxmc.world.generator.populator.TaigaTreePopulator

/**
 * @author LucGamesYT
 * @version 1.0
 */
open class TaigaBiome : GrassyBiome() {
    init {
        val treePopulator = TaigaTreePopulator()
        treePopulator.setBaseAmount(10)
        addPopulator(treePopulator)
    }
}
