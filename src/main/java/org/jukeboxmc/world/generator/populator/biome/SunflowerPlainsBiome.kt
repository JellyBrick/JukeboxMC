package org.jukeboxmc.world.generator.populator.biome

/**
 * @author LucGamesYT
 * @version 1.0
 */
class SunflowerPlainsBiome : GrassyBiome() {
    init {
        val sunflowerPopulator = SunflowerPopulator()
        sunflowerPopulator.setBaseAmount(2)
        sunflowerPopulator.setRandomAmount(3)
        addPopulator(sunflowerPopulator)
    }
}