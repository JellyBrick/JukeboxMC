package org.jukeboxmc.world.generator.populator

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockDoublePlant
import org.jukeboxmc.block.data.PlantType
import org.jukeboxmc.world.World
import org.jukeboxmc.world.chunk.Chunk
import org.jukeboxmc.world.chunk.manager.PopulationChunkManager
import java.util.Random

/**
 * @author LucGamesYT
 * @version 1.0
 */
class SunflowerPopulator : Populator() {
    private var randomAmount = 0
    private var baseAmount = 0
    private val BLOCK_SUNFLOWER: Block = Block.create<Block>(BlockType.DOUBLE_PLANT)
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
                chunk.setBlock(
                    x,
                    y,
                    z,
                    0,
                    (BLOCK_SUNFLOWER.clone() as BlockDoublePlant).setPlantType(PlantType.SUNFLOWER).setUpperBlock(false),
                )
                chunk.setBlock(
                    x,
                    y + 1,
                    z,
                    0,
                    (BLOCK_SUNFLOWER.clone() as BlockDoublePlant).setPlantType(PlantType.SUNFLOWER).setUpperBlock(true),
                )
            }
        }
    }

    private fun canFlowerStay(chunk: Chunk, x: Int, y: Int, z: Int): Boolean {
        val block = chunk.getBlock(x, y, z, 0)
        val val1 = block.type == BlockType.AIR || block.type == BlockType.TALLGRASS
        val val2 = blockBelow(chunk, x, y, z, BlockType.GRASS)
        return val1 && val2
    }

    fun setRandomAmount(randomAmount: Int) {
        this.randomAmount = randomAmount
    }

    fun setBaseAmount(baseAmount: Int) {
        this.baseAmount = baseAmount
    }
}
