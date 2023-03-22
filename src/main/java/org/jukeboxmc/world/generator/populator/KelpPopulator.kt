package org.jukeboxmc.world.generator.populator

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockKelp
import org.jukeboxmc.world.World
import org.jukeboxmc.world.chunk.Chunk
import org.jukeboxmc.world.chunk.manager.PopulationChunkManager
import org.jukeboxmc.world.generator.NormalGenerator
import java.util.Random

/**
 * @author LucGamesYT
 * @version 1.0
 */
class KelpPopulator : Populator() {
    private var randomAmount = 0
    private var baseAmount = 0
    private val BLOCK_KELP: Block = Block.create(BlockType.KELP)
    private val BLOCK_WATER: Block = Block.create(BlockType.WATER)
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
            if (y > 0) {
                if (!canKelpStay(chunk, x, y + 1, z)) {
                    return
                }
                if (!chunk.getBlock(x, y - 1, z, 0).isSolid) {
                    if (chunk.getBlock(x, y - 1, z, 0).type != BlockType.KELP) {
                        return
                    }
                }
                val height = random.nextInt(10) + 1
                for (h in 0..height) {
                    if (canKelpStay(chunk, x, y + h, z)) {
                        if (h == height || chunk.getBlock(x, y + h + 2, z, 0).type != BlockType.WATER) {
                            chunk.setBlock(
                                x,
                                y + h,
                                z,
                                0,
                                (BLOCK_KELP.clone() as BlockKelp).setKelpAge(20 + random.nextInt(4)),
                            )
                            chunk.setBlock(x, y + h, z, 1, BLOCK_WATER)
                            return
                        } else {
                            chunk.setBlock(x, y + h, z, 0, (BLOCK_KELP.clone() as BlockKelp).setKelpAge(25))
                            chunk.setBlock(x, y + h, z, 1, BLOCK_WATER)
                        }
                    } else {
                        return
                    }
                }
            }
        }
    }

    override fun getHighestWorkableBlock(chunk: Chunk, x: Int, z: Int): Int {
        var y: Int = NormalGenerator.WATER_HEIGHT - 1
        while (y >= 0) {
            val block = chunk.getBlock(x, y, z, 0)
            if (block.type != BlockType.AIR && block.type != BlockType.WATER && block.type != BlockType.FLOWING_WATER && block.type != BlockType.ICE && block.type != BlockType.PACKED_ICE && block.type != BlockType.BLUE_ICE) {
                break
            }
            --y
        }
        return if (y == 0) -1 else ++y
    }

    fun canKelpStay(chunk: Chunk, x: Int, y: Int, z: Int): Boolean {
        return chunk.getBlock(x, y, z, 0).type == BlockType.WATER
    }

    fun setRandomAmount(randomAmount: Int) {
        this.randomAmount = randomAmount
    }

    fun setBaseAmount(baseAmount: Int) {
        this.baseAmount = baseAmount
    }
}
