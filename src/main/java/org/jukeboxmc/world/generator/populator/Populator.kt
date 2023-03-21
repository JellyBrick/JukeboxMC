package org.jukeboxmc.world.generator.populator

import java.util.Random
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.world.World
import org.jukeboxmc.world.chunk.Chunk
import org.jukeboxmc.world.chunk.manager.PopulationChunkManager

/**
 * @author LucGamesYT
 * @version 1.0
 */
abstract class Populator {
    abstract fun populate(random: Random, world: World?, chunkManager: PopulationChunkManager, chunkX: Int, chunkZ: Int)
    fun blockBelow(chunk: Chunk, x: Int, y: Int, z: Int, blockType: BlockType): Boolean {
        return chunk.getBlock(x, y - 1, z, 0).type == blockType
    }

    open fun getHighestWorkableBlock(chunk: Chunk, x: Int, z: Int): Int {
        var y = 255
        while (y >= 0) {
            val block = chunk.getBlock(x, y, z, 0)
            if (block.type != BlockType.AIR && block.type != BlockType.LEAVES && block.type != BlockType.LEAVES2 && block.type != BlockType.AZALEA_LEAVES && block.type != BlockType.AZALEA_LEAVES_FLOWERED && block.type != BlockType.MANGROVE_LEAVES && block.type != BlockType.SNOW_LAYER) {
                break
            }
            --y
        }
        return if (y == 0) -1 else ++y
    }
}