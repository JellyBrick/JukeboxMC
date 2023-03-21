package org.jukeboxmc.world.generator.populator

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockRedFlower
import org.jukeboxmc.block.data.FlowerType
import org.jukeboxmc.world.World
import org.jukeboxmc.world.chunk.Chunk
import org.jukeboxmc.world.chunk.manager.PopulationChunkManager
import java.util.Random

/**
 * @author LucGamesYT
 * @version 1.0
 */
class FlowerPopulator : Populator() {
    private var randomAmount = 0
    private var baseAmount = 0
    private var flowerTypes: List<FlowerType>? = null
    private val BLOCK_FLOWER: Block = Block.create<Block>(BlockType.RED_FLOWER)
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
            if (canFlowerStay(chunk, x, y, z)) {
                val flowerType = flowerTypes!![random.nextInt(flowerTypes!!.size)]
                chunk.setBlock(x, y, z, 0, (BLOCK_FLOWER.clone() as BlockRedFlower).setFlowerType(flowerType))
            }
        }
    }

    private fun canFlowerStay(chunk: Chunk, x: Int, y: Int, z: Int): Boolean {
        val block = chunk.getBlock(x, y, z, 0)
        val val1 =
            block.type == BlockType.AIR || block.type == BlockType.SNOW_LAYER || block.type == BlockType.TALLGRASS
        val val2 = blockBelow(chunk, x, y, z, BlockType.GRASS)
        return val1 && val2
    }

    fun setRandomAmount(randomAmount: Int) {
        this.randomAmount = randomAmount
    }

    fun setBaseAmount(baseAmount: Int) {
        this.baseAmount = baseAmount
    }

    fun setFlowerTypes(flowerTypes: List<FlowerType>?) {
        this.flowerTypes = flowerTypes
    }
}
