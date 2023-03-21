package org.jukeboxmc.world.generator.populator

import org.jukeboxmc.world.World
import org.jukeboxmc.world.chunk.manager.PopulationChunkManager
import org.jukeboxmc.world.generator.`object`.OreType
import java.util.Random
import org.jukeboxmc.world.generator.`object`.Ore

/**
 * @author LucGamesYT
 * @version 1.0
 */
class OrePopulator(oreTypes: Array<OreType>) : Populator() {
    private val oreTypes: Array<OreType>

    init {
        this.oreTypes = oreTypes
    }

    override fun populate(
        random: Random,
        world: World?,
        chunkManager: PopulationChunkManager,
        chunkX: Int,
        chunkZ: Int,
    ) {
        for (oreType in oreTypes) {
            val ore = Ore(random, oreType)
            for (i in 0 until oreType.clusterCount) {
                val x = random.nextInt(15) + chunkX * 16
                val y: Int = random.nextInt(oreType.maxHeight - oreType.minHeight) + oreType.minHeight
                val z = random.nextInt(15) + chunkZ * 16
                if (ore.canPlace(chunkManager, x, y, z)) {
                    ore.place(chunkManager, x, y, z)
                }
            }
        }
    }
}
