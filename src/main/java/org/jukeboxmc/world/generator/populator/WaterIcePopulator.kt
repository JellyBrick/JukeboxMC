package org.jukeboxmc.world.generator.populator

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.world.World
import org.jukeboxmc.world.chunk.manager.PopulationChunkManager
import java.util.Random

/**
 * @author LucGamesYT
 * @version 1.0
 */
class WaterIcePopulator : Populator() {
    private val BLOCK_ICE: Block = Block.create(BlockType.ICE)
    override fun populate(
        random: Random,
        world: World?,
        chunkManager: PopulationChunkManager,
        chunkX: Int,
        chunkZ: Int,
    ) {
        val chunk = chunkManager.getChunk(chunkX, chunkZ)
        for (x in 0..15) {
            for (z in 0..15) {
                val biome = chunk!!.getBiome(x, 7, z)
                if (biome != null && biome.isFreezing) {
                    val y = chunk.getHighestBlockY(x, z) - 1
                    if (chunk.getBlock(x, y, z, 0).type == BlockType.WATER) {
                        chunk.setBlock(x, y, z, 0, BLOCK_ICE)
                    }
                }
            }
        }
    }
}
