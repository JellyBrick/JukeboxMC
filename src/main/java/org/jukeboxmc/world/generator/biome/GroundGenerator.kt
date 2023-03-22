package org.jukeboxmc.world.generator.biome

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.world.chunk.Chunk
import org.jukeboxmc.world.generator.NormalGenerator
import java.util.Random
import kotlin.math.max

/**
 * @author LucGamesYT
 * @version 1.0
 */
open class GroundGenerator {
    protected var topMaterial: Block
    protected var groundMaterial: Block
    private val BLOCK_AIR: Block = Block.create(BlockType.AIR)
    private val BLOCK_STONE: Block = Block.create(BlockType.STONE)
    private val BLOCK_GRAVEL: Block = Block.create(BlockType.GRAVEL)
    private val BLOCK_SANDSTONE: Block = Block.create(BlockType.SANDSTONE)

    init {
        topMaterial = Block.create(BlockType.GRASS)
        groundMaterial = Block.create(BlockType.DIRT)
    }

    open fun generateTerrainColumn(chunk: Chunk, random: Random, chunkX: Int, chunkZ: Int, surfaceNoise: Double) {
        val seaLevel: Int = NormalGenerator.WATER_HEIGHT
        var topMaterial = topMaterial
        var groundMaterial = groundMaterial
        val x = chunkX and 0xF
        val z = chunkZ and 0xF
        val surfaceHeight = max((surfaceNoise / 3.0 + 3.0 + random.nextDouble() * 0.25).toInt(), 1)
        var deep = -1
        for (y in 255 downTo 0) {
            val blockType = chunk.getBlock(x, y, z, 0).type
            if (blockType == BlockType.AIR) {
                deep = -1
            } else if (blockType == BlockType.STONE) {
                if (deep == -1) {
                    if (y >= seaLevel - 5 && y <= seaLevel) {
                        topMaterial = this.topMaterial
                        groundMaterial = this.groundMaterial
                    }
                    deep = surfaceHeight
                    if (y >= seaLevel - 2) {
                        chunk.setBlock(x, y, z, 0, topMaterial)
                    } else if (y < seaLevel - 8 - surfaceHeight) {
                        topMaterial = BLOCK_AIR
                        groundMaterial = BLOCK_STONE
                        chunk.setBlock(x, y, z, 0, BLOCK_GRAVEL)
                    } else {
                        chunk.setBlock(x, y, z, 0, groundMaterial)
                    }
                } else if (deep > 0) {
                    deep--
                    chunk.setBlock(x, y, z, 0, groundMaterial)
                    if (deep == 0 && groundMaterial.type == BlockType.SAND) {
                        deep = random.nextInt(4) + max(0, y - seaLevel - 1)
                        groundMaterial = BLOCK_SANDSTONE
                    }
                }
            }
        }
    }
}
