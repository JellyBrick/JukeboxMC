package org.jukeboxmc.world.generator.populator.biome

import org.jukeboxmc.world.generator.`object`.tree.Tree

/**
 * @author LucGamesYT
 * @version 1.0
 */
open class ForestBiome : GrassyBiome() {
    init {
        var treePopulator = TreePopulator(Tree.TreeType.BIRCH)
        treePopulator.setBaseAmount(3)
        addPopulator(treePopulator)
        treePopulator = TreePopulator(Tree.TreeType.OAK)
        treePopulator.setBaseAmount(3)
        addPopulator(treePopulator)
    }
}