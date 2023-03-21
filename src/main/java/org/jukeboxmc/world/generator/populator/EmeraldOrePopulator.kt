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
class EmeraldOrePopulator : Populator() {
    private val BLOCK_EMERALD_ORE: Block = Block.create<Block>(BlockType.EMERALD_ORE)
    override fun populate(
        random: Random,
        world: World?,
        chunkManager: PopulationChunkManager,
        chunkX: Int,
        chunkZ: Int,
    ) {
        val chunk = chunkManager.getChunk(chunkX, chunkZ)
        for (i in 0..10) {
            val x = random.nextInt(16)
            val z = random.nextInt(16)
            val y = random.nextInt(0, 32)
            if (chunk!!.getBlock(x, y, z, 0).type != BlockType.STONE) {
                continue
            }
            chunk.setBlock(x, y, z, 0, BLOCK_EMERALD_ORE)
        }
    }
}
