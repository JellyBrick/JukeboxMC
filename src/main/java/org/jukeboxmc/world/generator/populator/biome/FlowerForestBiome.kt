package org.jukeboxmc.world.generator.populator.biome

import java.util.List
import org.jukeboxmc.block.data.FlowerType
import org.jukeboxmc.world.generator.populator.FlowerPopulator

/**
 * @author LucGamesYT
 * @version 1.0
 */
class FlowerForestBiome : ForestBiome() {
    init {
        val flowerPopulator = FlowerPopulator()
        flowerPopulator.setFlowerTypes(listOf(*FlowerType.values()))
        flowerPopulator.setBaseAmount(10)
        addPopulator(flowerPopulator)
    }
}