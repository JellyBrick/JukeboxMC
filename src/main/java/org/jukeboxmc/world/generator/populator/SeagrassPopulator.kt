package org.jukeboxmc.world.generator.populator

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockSeagrass
import org.jukeboxmc.block.data.SeaGrassType
import org.jukeboxmc.world.World
import org.jukeboxmc.world.chunk.Chunk
import org.jukeboxmc.world.chunk.manager.PopulationChunkManager
import org.jukeboxmc.world.generator.NormalGenerator
import java.util.Random

/**
 * @author LucGamesYT
 * @version 1.0
 */
class SeagrassPopulator(private val tallSeagrassProbability: Double) : Populator() {
    private var randomAmount = 0
    private var baseAmount = 0
    private val BLOCK_SEAGRASS: Block = Block.create(BlockType.SEAGRASS)
    private val BLOCK_WATER: Block = Block.create(BlockType.WATER)
    override fun populate(
        random: Random,
        world: World?,
        chunkManager: PopulationChunkManager,
        chunkX: Int,
        chunkZ: Int,
    ) {
        val chunk = chunkManager.getChunk(chunkX, chunkZ)
        val amount = random.nextInt(randomAmount + 1) + baseAmount
        for (i in 0 until amount) {
            val x = random.nextInt(16)
            val z = random.nextInt(16)
            val y = getHighestWorkableBlock(chunk!!, x, z)
            if (y > 0 && canSeagrassStay(chunk, x, y, z, false)) {
                if (random.nextDouble() < tallSeagrassProbability) {
                    if (canSeagrassStay(chunk, x, y + 1, z, true)) {
                        chunk.setBlock(
                            x,
                            y,
                            z,
                            0,
                            (BLOCK_SEAGRASS.clone() as BlockSeagrass).setSeaGrassType(SeaGrassType.DOUBLE_BOT),
                        )
                        chunk.setBlock(x, y, z, 1, BLOCK_WATER)
                        chunk.setBlock(
                            x,
                            y + 1,
                            z,
                            0,
                            (BLOCK_SEAGRASS.clone() as BlockSeagrass).setSeaGrassType(SeaGrassType.DOUBLE_TOP),
                        )
                        chunk.setBlock(x, y + 1, z, 1, BLOCK_WATER)
                    }
                } else {
                    chunk.setBlock(
                        x,
                        y,
                        z,
                        0,
                        (BLOCK_SEAGRASS.clone() as BlockSeagrass).setSeaGrassType(SeaGrassType.DEFAULT),
                    )
                    chunk.setBlock(x, y, z, 1, BLOCK_WATER)
                }
            }
        }
    }

    override fun getHighestWorkableBlock(chunk: Chunk, x: Int, z: Int): Int {
        var y: Int
        y = NormalGenerator.WATER_HEIGHT - 1
        while (y >= 0) {
            val block = chunk.getBlock(x, y, z, 0)
            if (block.type != BlockType.AIR && block.type != BlockType.WATER && block.type != BlockType.FLOWING_WATER && block.type != BlockType.ICE && block.type != BlockType.PACKED_ICE && block.type != BlockType.BLUE_ICE) {
                break
            }
            --y
        }
        return if (y == 0) -1 else ++y
    }

    fun canSeagrassStay(chunk: Chunk, x: Int, y: Int, z: Int, tallSeagrass: Boolean): Boolean {
        return if (tallSeagrass) {
            chunk.getBlock(x, y, z, 0).type == BlockType.WATER
        } else {
            chunk.getBlock(x, y, z, 0).type == BlockType.WATER && chunk.getBlock(
                x,
                y - 1,
                z,
                0,
            ).isSolid
        }
    }

    fun setRandomAmount(randomAmount: Int) {
        this.randomAmount = randomAmount
    }

    fun setBaseAmount(baseAmount: Int) {
        this.baseAmount = baseAmount
    }
}
