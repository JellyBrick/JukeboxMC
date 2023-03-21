package org.jukeboxmc.world.generator.populator

import java.util.Random
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.world.World
import org.jukeboxmc.world.chunk.Chunk
import org.jukeboxmc.world.chunk.manager.PopulationChunkManager

/**
 * @author LucGamesYT
 * @version 1.0
 */
class CactusPopulator : Populator() {
    private var randomAmount = 0
    private var baseAmount = 0
    private val BLOCK_CACTUS: Block = Block.Companion.create<Block>(BlockType.CACTUS)
    override fun populate(
        random: Random,
        world: World?,
        chunkManager: PopulationChunkManager,
        chunkX: Int,
        chunkZ: Int
    ) {
        val chunk = chunkManager.getChunk(chunkX, chunkZ)
        val amount = random.nextInt(randomAmount + 1) + baseAmount
        for (i in 0 until amount) {
            val x = random.nextInt(16)
            val z = random.nextInt(16)
            var y = getHighestWorkableBlock(chunk!!, x, z)
            var height = 0
            val range = random.nextInt(18)
            if (range >= 16) {
                height = 2
            } else if (range >= 11) {
                height = 1
            }
            if (y > 0) {
                for (yy in 0 until height) {
                    y += yy
                    if (canStayCactus(chunk, x, y, z)) {
                        chunk.setBlock(x, y, z, 0, BLOCK_CACTUS)
                    }
                }
            }
        }
    }

    fun canStayCactus(chunk: Chunk, x: Int, y: Int, z: Int): Boolean {
        val block = chunk.getBlock(x, y, z, 0)
        val val1 =
            block.type == BlockType.AIR || block.type == BlockType.SNOW_LAYER || block.type == BlockType.TALLGRASS
        val val2 = blockBelow(chunk, x, y, z, BlockType.SAND)
        val val3 = blockBelow(chunk, x, y, z, BlockType.CACTUS)
        val val4 = isAirAround(chunk, x, y, z)
        return val1 && val2 && val4 || val3
    }

    private fun isAirAround(chunk: Chunk, x: Int, y: Int, z: Int): Boolean {
        val val1 = chunk.getBlock(x + 1, y, z, 0).type == BlockType.AIR
        val val2 = chunk.getBlock(x - 1, y, z, 0).type == BlockType.AIR
        val val3 = chunk.getBlock(x, y, z + 1, 0).type == BlockType.AIR
        val val4 = chunk.getBlock(x, y, z - 1, 0).type == BlockType.AIR
        return val1 && val2 && val3 && val4
    }

    fun setRandomAmount(randomAmount: Int) {
        this.randomAmount = randomAmount
    }

    fun setBaseAmount(baseAmount: Int) {
        this.baseAmount = baseAmount
    }
}