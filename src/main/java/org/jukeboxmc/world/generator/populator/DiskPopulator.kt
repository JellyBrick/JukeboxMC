package org.jukeboxmc.world.generator.populator

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.world.World
import org.jukeboxmc.world.chunk.Chunk
import org.jukeboxmc.world.chunk.manager.PopulationChunkManager
import org.jukeboxmc.world.generator.NormalGenerator
import java.util.Random

/**
 * @author LucGamesYT
 * @version 1.0
 */
class DiskPopulator(
    private val probability: Double,
    private val sourceBlock: Block,
    private val radiusMin: Int,
    private val radiusMax: Int,
    private val radiusY: Int,
    private val replaceBlocks: List<BlockType>,
) : Populator() {
    private var randomAmount = 0
    private var baseAmount = 0
    override fun populate(
        random: Random,
        world: World?,
        chunkManager: PopulationChunkManager,
        chunkX: Int,
        chunkZ: Int,
    ) {
        val amount = random.nextInt(randomAmount + 1) + baseAmount
        val chunk = chunkManager.getChunk(chunkX, chunkZ) ?: return // FIXME
        for (i in 0 until amount) {
            if (random.nextDouble() >= probability) {
                return
            }
            val sourceX = (chunk.x shl 4) + random.nextInt(16)
            val sourceZ = (chunk.z shl 4) + random.nextInt(16)
            val sourceY = getHighestWorkableBlock(chunk, sourceX, sourceZ) - 1
            if (sourceY < radiusY) {
                return
            }
            if (chunk.getBlock(sourceX, sourceY + 1, sourceZ, 0).type != BlockType.WATER) {
                return
            }
            val radius = random.nextInt(radiusMin, radiusMax)
            for (xx in sourceX - radius..sourceX + radius) {
                for (zz in sourceZ - radius..sourceZ + radius) {
                    if ((xx - sourceX) * (xx - sourceX) + (zz - sourceZ) * (zz - sourceZ) <= radius * radius) {
                        for (yy in sourceY - radiusY..sourceY + radiusY) {
                            for (replaceBlockState in replaceBlocks) {
                                if (chunk.getBlock(xx, yy, zz, 0).type == replaceBlockState) {
                                    chunk.setBlock(xx, yy, zz, 0, sourceBlock)
                                }
                            }
                        }
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

    fun setRandomAmount(randomAmount: Int) {
        this.randomAmount = randomAmount
    }

    fun setBaseAmount(baseAmount: Int) {
        this.baseAmount = baseAmount
    }
}
