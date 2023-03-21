package org.jukeboxmc.world.generator.populator.biome

import org.jukeboxmc.world.generator.`object`.tree.Tree
import org.jukeboxmc.world.generator.populator.EmeraldOrePopulator
import org.jukeboxmc.world.generator.populator.TreePopulator

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ExtremeHillsBiome : BiomePopulator() {
    init {
        addPopulator(EmeraldOrePopulator())
        val treePopulator = TreePopulator(Tree.TreeType.SPRUCE)
        treePopulator.setBaseAmount(2)
        treePopulator.setRandomAmount(2)
        addPopulator(treePopulator)
    }
}