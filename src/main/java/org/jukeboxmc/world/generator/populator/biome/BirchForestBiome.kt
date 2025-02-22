package org.jukeboxmc.world.generator.populator.biome

import org.jukeboxmc.world.generator.populator.TreePopulator
import org.jukeboxmc.world.generator.thing.tree.Tree

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BirchForestBiome : GrassyBiome() {
    init {
        val treePopulator = TreePopulator(Tree.TreeType.BIRCH)
        treePopulator.setBaseAmount(6)
        addPopulator(treePopulator)
    }
}
