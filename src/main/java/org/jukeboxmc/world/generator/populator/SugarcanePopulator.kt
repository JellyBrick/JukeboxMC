package org.jukeboxmc.world.generator.populator

import java.util.Random
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.world.World
import org.jukeboxmc.world.chunk.Chunk
import org.jukeboxmc.world.chunk.manager.PopulationChunkManager

/**
 * @author LucGamesYT
 * @version 1.0
 */
class SugarcanePopulator : Populator() {
    private var randomAmount = 0
    private var baseAmount = 0
    private val BLOCK_SUGAR_CANE: Block = Block.create<Block>(BlockType.SUGAR_CANE)
    override fun populate(
        random: Random,
        world: World?,
        chunkManager: PopulationChunkManager,
        chunkX: Int,
        chunkZ: Int
    ) {
        val amount = random.nextInt(randomAmount + 1) + baseAmount
        val chunk = chunkManager.getChunk(chunkX, chunkZ)
        for (i in 0 until amount) {
            val x = random.nextInt(16)
            val z = random.nextInt(16)
            val y = getHighestWorkableBlock(chunk!!, x, z)
            if (y != -1 && canSugarCaneStay(chunk, x, y, z)) {
                chunk.setBlock(x, y, z, 0, BLOCK_SUGAR_CANE)
            }
        }
    }

    private fun canSugarCaneStay(chunk: Chunk, x: Int, y: Int, z: Int): Boolean {
        val block = chunk.getBlock(x, y, z, 0)
        val val1 = block.type == BlockType.AIR || block.type == BlockType.SNOW_LAYER
        val val2 = blockBelow(chunk, x, y, z, BlockType.GRAVEL) || blockBelow(chunk, x, y, z, BlockType.GRASS)
        val val3 = findWater(chunk, x, y - 1, z)
        return val1 && val2 && val3
    }

    fun setRandomAmount(randomAmount: Int) {
        this.randomAmount = randomAmount
    }

    fun setBaseAmount(baseAmount: Int) {
        this.baseAmount = baseAmount
    }

    private fun findWater(chunk: Chunk, x: Int, y: Int, z: Int): Boolean {
        for (i in x - 4 until x + 4) {
            for (j in z - 4 until z + 4) {
                val block = chunk.getBlock(i, y, j, 0)
                val blockNorth = block.getSide(BlockFace.NORTH)
                val blockEast = block.getSide(BlockFace.EAST)
                val blockSouth = block.getSide(BlockFace.SOUTH)
                val blockWest = block.getSide(BlockFace.WEST)
                if (blockNorth.type == BlockType.WATER || blockEast.type == BlockType.WATER || blockSouth.type == BlockType.WATER || blockWest.type == BlockType.WATER) {
                    return true
                }
            }
        }
        return false
    }
}