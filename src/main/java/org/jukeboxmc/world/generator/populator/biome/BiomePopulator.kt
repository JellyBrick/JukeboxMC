package org.jukeboxmc.world.generator.populator.biome

/**
 * @author LucGamesYT
 * @version 1.0
 */
open class BiomePopulator {
    private val populators: MutableList<Populator> = ArrayList<Populator>()
    fun addPopulator(populator: Populator) {
        populators.add(populator)
    }

    fun getPopulators(): List<Populator> {
        return populators
    }
}