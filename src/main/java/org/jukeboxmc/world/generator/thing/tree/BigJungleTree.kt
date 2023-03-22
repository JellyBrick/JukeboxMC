package org.jukeboxmc.world.generator.thing.tree

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockLeaves
import org.jukeboxmc.block.behavior.BlockLog
import org.jukeboxmc.block.behavior.BlockVine
import org.jukeboxmc.block.data.LeafType
import org.jukeboxmc.block.data.LogType
import org.jukeboxmc.math.Vector
import org.jukeboxmc.world.chunk.manager.PopulationChunkManager
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * @author LucGamesYT
 * @version 1.0
 */
open class BigJungleTree(private val baseHeight: Int, private val extraRandomHeight: Int) {
    private val BLOCK_LOG: Block = Block.create<BlockLog>(BlockType.LOG).setLogType(LogType.JUNGLE)
    private val BLOCK_LEAVES: Block = Block.create<BlockLeaves>(BlockType.LEAVES).setLeafType(LeafType.JUNGLE)
    private val BLOCK_VINE: Block = Block.create(BlockType.VINE)
    private val BLOCK_DIRT: Block = Block.create(BlockType.DIRT)
    fun create(random: Random, manager: PopulationChunkManager, position: Vector) {
        val height = getHeight(random)
        if (ensureGrowable(manager, random, position, height)) {
            createCrown(manager, position.add(0f, height.toFloat(), 0f), 2)
            var j = position.y.toInt() + height - 2 - random.nextInt(4)
            while (j > position.y + height / 2) {
                val f = random.nextFloat() * (Math.PI * 2f)
                var k = (position.x + (0.5f + cos(f) * 4.0f)).toInt()
                var l = (position.z + (0.5f + sin(f) * 4.0f)).toInt()
                for (i1 in 0..4) {
                    k = (position.x + (1.5f + cos(f) * i1)).toInt()
                    l = (position.z + (1.5f + sin(f) * i1)).toInt()
                    manager.setBlock(Vector(k, j - 3 + i1 / 2, l), BLOCK_LOG)
                }
                val j2 = 1 + random.nextInt(2)
                for (k1 in j - j2..j) {
                    val l1 = k1 - j
                    growLeavesLayer(manager, Vector(k, k1, l), 1 - l1)
                }
                j -= 2 + random.nextInt(4)
            }
            for (i2 in 0 until height) {
                val blockPosition = position.add(0f, i2.toFloat(), 0f)
                if (canPlace(manager.getBlock(blockPosition).type)) {
                    manager.setBlock(blockPosition, BLOCK_LOG)
                    if (i2 > 0) {
                        placeVine(manager, random, blockPosition.west(), 8)
                        placeVine(manager, random, blockPosition.north(), 1)
                    }
                }
                if (i2 < height - 1) {
                    val blockPosition1 = blockPosition.east()
                    if (canPlace(manager.getBlock(blockPosition1).type)) {
                        manager.setBlock(blockPosition1, BLOCK_LOG)
                        if (i2 > 0) {
                            placeVine(manager, random, blockPosition1.east(), 2)
                            placeVine(manager, random, blockPosition1.north(), 1)
                        }
                    }
                    val blockPosition2 = blockPosition.south().east()
                    if (canPlace(manager.getBlock(blockPosition2).type)) {
                        manager.setBlock(blockPosition2, BLOCK_LOG)
                        if (i2 > 0) {
                            placeVine(manager, random, blockPosition2.east(), 2)
                            placeVine(manager, random, blockPosition2.south(), 4)
                        }
                    }
                    val blockPosition3 = blockPosition.south()
                    if (canPlace(manager.getBlock(blockPosition3).type)) {
                        manager.setBlock(blockPosition3, BLOCK_LOG)
                        if (i2 > 0) {
                            placeVine(manager, random, blockPosition3.west(), 8)
                            placeVine(manager, random, blockPosition3.south(), 4)
                        }
                    }
                }
            }
        }
    }

    fun ensureGrowable(manager: PopulationChunkManager, random: Random?, position: Vector, height: Int): Boolean {
        return isSpaceAt(manager, position, height) && ensureDirtsUnderneath(position, manager)
    }

    private fun ensureDirtsUnderneath(pos: Vector, manager: PopulationChunkManager): Boolean {
        val blockPosition = pos.down()
        val block = manager.getBlock(blockPosition).type
        return if ((block == BlockType.GRASS || block == BlockType.DIRT) && pos.y >= 2) {
            manager.setBlock(blockPosition, BLOCK_DIRT)
            manager.setBlock(blockPosition.east(), BLOCK_DIRT)
            manager.setBlock(blockPosition.south(), BLOCK_DIRT)
            manager.setBlock(blockPosition.south().east(), BLOCK_DIRT)
            true
        } else {
            false
        }
    }

    private fun isSpaceAt(manager: PopulationChunkManager, leavesPos: Vector, height: Int): Boolean {
        var flag = true
        return if (leavesPos.y >= 1 && leavesPos.y + height + 1 <= 256) {
            for (i in 0..1 + height) {
                var j = 2
                if (i == 0) {
                    j = 1
                } else if (i >= 1 + height - 2) {
                    j = 2
                }
                var k = -j
                while (k <= j && flag) {
                    var l = -j
                    while (l <= j && flag) {
                        val blockPos = leavesPos.add(k.toFloat(), i.toFloat(), l.toFloat())
                        if (leavesPos.y + i < 0 || leavesPos.y + i >= 256 || !canPlace(
                                manager.getBlock(
                                    blockPos,
                                ).type,
                            )
                        ) {
                            flag = false
                        }
                        ++l
                    }
                    ++k
                }
            }
            flag
        } else {
            false
        }
    }

    fun createCrown(manager: PopulationChunkManager, position: Vector, value: Int) {
        for (j in -2..0) {
            growLeavesLayerStrict(manager, position.add(0f, j.toFloat(), 0f), value + 1 - j)
        }
    }

    fun canPlace(blockType: BlockType?): Boolean {
        return blockType == BlockType.AIR || blockType == BlockType.LEAVES || blockType == BlockType.GRASS || blockType == BlockType.DIRT || blockType == BlockType.LOG || blockType == BlockType.LOG2 || blockType == BlockType.SAPLING || blockType == BlockType.VINE
    }

    fun growLeavesLayer(manager: PopulationChunkManager, position: Vector, width: Int) {
        val i = width * width
        for (j in -width..width + 1) {
            for (k in -width..width + 1) {
                val l = j - 1
                val i1 = k - 1
                if (j * j + k * k <= i || l * l + i1 * i1 <= i || j * j + i1 * i1 <= i || l * l + k * k <= i) {
                    val blockPosition = position.add(j.toFloat(), 0f, k.toFloat())
                    val id = manager.getBlock(blockPosition).type
                    if (id == BlockType.AIR || id == BlockType.LEAVES) {
                        manager.setBlock(blockPosition, BLOCK_LEAVES)
                    }
                }
            }
        }
    }

    protected fun growLeavesLayerStrict(manager: PopulationChunkManager, position: Vector, width: Int) {
        val i = width * width
        for (j in -width..width + 1) {
            for (k in -width..width + 1) {
                val l = j - 1
                val i1 = k - 1
                if (j * j + k * k <= i || l * l + i1 * i1 <= i || j * j + i1 * i1 <= i || l * l + k * k <= i) {
                    val blockPosition = position.add(j.toFloat(), 0f, k.toFloat())
                    val id = manager.getBlock(blockPosition).type
                    if (id == BlockType.AIR || id == BlockType.LEAVES) {
                        manager.setBlock(blockPosition, BLOCK_LEAVES)
                    }
                }
            }
        }
    }

    fun placeVine(manager: PopulationChunkManager, random: Random, position: Vector, meta: Int) {
        if (random.nextInt(3) > 0 && manager.getBlock(position).type == BlockType.AIR) {
            manager.setBlock(position, (BLOCK_VINE.clone() as BlockVine).setVineDirection(meta))
        }
    }

    private fun getHeight(rand: Random): Int {
        var height = rand.nextInt(3) + baseHeight
        if (extraRandomHeight > 1) {
            height += rand.nextInt(extraRandomHeight)
        }
        return height
    }
}
