package org.jukeboxmc.world.generator.populator

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.world.World
import org.jukeboxmc.world.chunk.Chunk
import org.jukeboxmc.world.chunk.manager.PopulationChunkManager
import java.util.Random

/**
 * @author LucGamesYT
 * @version 1.0
 */
class TallGrassPopulator : Populator() {
    private var randomAmount = 0
    private var baseAmount = 0
    private val BLOCK_TALLGRASS: Block = Block.create(BlockType.TALLGRASS)
    override fun populate(
        random: Random,
        world: World?,
        chunkManager: PopulationChunkManager,
        chunkX: Int,
        chunkZ: Int,
    ) {
        val amount = random.nextInt(randomAmount + 1) + baseAmount
        val chunk = chunkManager.getChunk(chunkX, chunkZ)
        for (i in 0 until amount) {
            val x = random.nextInt(16)
            val z = random.nextInt(16)
            val y = getHighestWorkableBlock(chunk!!, x, z)
            if (y != -1 && canTallGrassStay(chunk, x, y, z)) {
                chunk.setBlock(x, y, z, 0, BLOCK_TALLGRASS)
            }
        }
    }

    fun setRandomAmount(randomAmount: Int) {
        this.randomAmount = randomAmount
    }

    fun setBaseAmount(baseAmount: Int) {
        this.baseAmount = baseAmount
    }

    private fun canTallGrassStay(chunk: Chunk, x: Int, y: Int, z: Int): Boolean {
        val block = chunk.getBlock(x, y, z, 0)
        return (block.type == BlockType.AIR || block.type == BlockType.SNOW_LAYER) && blockBelow(
            chunk,
            x,
            y,
            z,
            BlockType.GRASS,
        )
    }
}
