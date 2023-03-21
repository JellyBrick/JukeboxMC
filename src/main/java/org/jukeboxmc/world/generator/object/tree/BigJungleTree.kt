package org.jukeboxmc.world.generator.`object`.tree

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockLeaves
import org.jukeboxmc.block.behavior.BlockLog
import org.jukeboxmc.block.behavior.BlockVine
import org.jukeboxmc.block.data.LeafType
import org.jukeboxmc.block.data.LogType
import org.jukeboxmc.math.Vector
import org.jukeboxmc.world.chunk.manager.PopulationChunkManager
import java.util.Random

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BigJungleTree(private val baseHeight: Int, private val extraRandomHeight: Int) {
    private val BLOCK_LOG: Block = Block.create<BlockLog>(BlockType.LOG).setLogType(LogType.JUNGLE)
    private val BLOCK_LEAVES: Block = Block.create<BlockLeaves>(BlockType.LEAVES).setLeafType(LeafType.JUNGLE)
    private val BLOCK_VINE: Block = Block.create<Block>(BlockType.VINE)
    private val BLOCK_DIRT: Block = Block.create<Block>(BlockType.DIRT)
    fun create(random: Random, manager: PopulationChunkManager, position: Vector) {
        val height = getHeight(random)
        if (ensureGrowable(manager, random, position, height)) {
            createCrown(manager, position.add(0f, height.toFloat(), 0f), 2)
            var j = position.getY().toInt() + height - 2 - random.nextInt(4)
            while (j > position.getY() + height / 2) {
                val f = random.nextFloat() * (Math.PI.toFloat() * 2f)
                var k = (position.getX() + (0.5f + Math.cos(f.toDouble()) * 4.0f)).toInt()
                var l = (position.getZ() + (0.5f + Math.sin(f.toDouble()) * 4.0f)).toInt()
                for (i1 in 0..4) {
                    k = (position.getX() + (1.5f + Math.cos(f.toDouble()) * i1.toFloat())).toInt()
                    l = (position.getZ() + (1.5f + Math.sin(f.toDouble()) * i1.toFloat())).toInt()
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
                val blockpos = position.add(0f, i2.toFloat(), 0f)
                if (canPlace(manager.getBlock(blockpos!!).type)) {
                    manager.setBlock(blockpos, BLOCK_LOG)
                    if (i2 > 0) {
                        placeVine(manager, random, blockpos.west(), 8)
                        placeVine(manager, random, blockpos.north(), 1)
                    }
                }
                if (i2 < height - 1) {
                    val blockpos1 = blockpos.east()
                    if (canPlace(manager.getBlock(blockpos1).type)) {
                        manager.setBlock(blockpos1, BLOCK_LOG)
                        if (i2 > 0) {
                            placeVine(manager, random, blockpos1.east(), 2)
                            placeVine(manager, random, blockpos1.north(), 1)
                        }
                    }
                    val blockpos2 = blockpos.south().east()
                    if (canPlace(manager.getBlock(blockpos2).type)) {
                        manager.setBlock(blockpos2, BLOCK_LOG)
                        if (i2 > 0) {
                            placeVine(manager, random, blockpos2.east(), 2)
                            placeVine(manager, random, blockpos2.south(), 4)
                        }
                    }
                    val blockpos3 = blockpos.south()
                    if (canPlace(manager.getBlock(blockpos3).type)) {
                        manager.setBlock(blockpos3, BLOCK_LOG)
                        if (i2 > 0) {
                            placeVine(manager, random, blockpos3.west(), 8)
                            placeVine(manager, random, blockpos3.south(), 4)
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
        val blockpos = pos.down()
        val block = manager.getBlock(blockpos).type
        return if ((block == BlockType.GRASS || block == BlockType.DIRT) && pos.getY() >= 2) {
            manager.setBlock(blockpos, BLOCK_DIRT)
            manager.setBlock(blockpos.east(), BLOCK_DIRT)
            manager.setBlock(blockpos.south(), BLOCK_DIRT)
            manager.setBlock(blockpos.south().east(), BLOCK_DIRT)
            true
        } else {
            false
        }
    }

    private fun isSpaceAt(manager: PopulationChunkManager, leavesPos: Vector, height: Int): Boolean {
        var flag = true
        return if (leavesPos.getY() >= 1 && leavesPos.getY() + height + 1 <= 256) {
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
                        if (leavesPos.getY() + i < 0 || leavesPos.getY() + i >= 256 || !canPlace(
                                manager.getBlock(
                                    blockPos!!,
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
                    val blockpos = position.add(j.toFloat(), 0f, k.toFloat())
                    val id = manager.getBlock(blockpos!!).type
                    if (id == BlockType.AIR || id == BlockType.LEAVES) {
                        manager.setBlock(blockpos, BLOCK_LEAVES)
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
                    val blockpos = position.add(j.toFloat(), 0f, k.toFloat())
                    val id = manager.getBlock(blockpos!!).type
                    if (id == BlockType.AIR || id == BlockType.LEAVES) {
                        manager.setBlock(blockpos, BLOCK_LEAVES)
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
