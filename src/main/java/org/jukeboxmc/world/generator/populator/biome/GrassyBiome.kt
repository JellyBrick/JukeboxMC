package org.jukeboxmc.world.generator.populator.biome

/**
 * @author LucGamesYT
 * @version 1.0
 */
open class GrassyBiome : BiomePopulator() {
    init {
        val tallGrassPopulator = TallGrassPopulator()
        tallGrassPopulator.setBaseAmount(30)
        addPopulator(tallGrassPopulator)
    }
}