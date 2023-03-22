package org.jukeboxmc.world.generator.thing.tree

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockLeaves2
import org.jukeboxmc.block.behavior.BlockLog2
import org.jukeboxmc.block.data.LeafType2
import org.jukeboxmc.block.data.LogType2
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.math.Vector
import org.jukeboxmc.world.chunk.manager.PopulationChunkManager
import java.util.*
import kotlin.math.abs

/**
 * @author LucGamesYT
 * @version 1.0
 */
class SavannaTree {
    private val BLOCK_ACACIA_LEAVES: Block =
        Block.create<BlockLeaves2>(BlockType.LEAVES2).setLeafType(LeafType2.ACACIA)
    private val BLOCK_ACACIA_LOG: Block = Block.create<BlockLog2>(BlockType.LOG2).setLogType(LogType2.ACACIA)
    private val BLOCK_DIRT: Block = Block.create(BlockType.DIRT)
    fun create(random: Random, manager: PopulationChunkManager, position: Vector) {
        val i = random.nextInt(3) + random.nextInt(3) + 5
        if (position.y >= 1 && position.y + i + 1 <= 256) {
            val down = position.down()
            val block = manager.getBlock(down).type
            if ((block == BlockType.GRASS || block == BlockType.DIRT) && position.y < 256 - i - 1) {
                manager.setBlock(position.down(), BLOCK_DIRT)
                val blockFace: BlockFace =
                    BlockFace.horizontal[random.nextInt(BlockFace.horizontal.size)]
                val k2 = i - random.nextInt(4) - 1
                var l2 = 3 - random.nextInt(3)
                var i3 = position.blockX
                var j1 = position.blockZ
                var k1 = 0
                for (l1 in 0 until i) {
                    val i2 = position.blockY + l1
                    if (l1 >= k2 && l2 > 0) {
                        i3 += blockFace.offset.blockX
                        j1 += blockFace.offset.blockZ
                        --l2
                    }
                    val blockpos = Vector(i3, i2, j1)
                    val material = manager.getBlock(blockpos).type
                    if (material == BlockType.AIR || material == BlockType.LEAVES2) {
                        manager.setBlock(blockpos, BLOCK_ACACIA_LOG)
                        k1 = i2
                    }
                }
                var blockpos2 = Vector(i3, k1, j1)
                for (j3 in -3..3) {
                    for (i4 in -3..3) {
                        if (abs(j3) != 3 || abs(i4) != 3) {
                            setLeaves(manager, blockpos2.add(j3.toFloat(), 0f, i4.toFloat()))
                        }
                    }
                }
                blockpos2 = blockpos2.up()
                for (k3 in -1..1) {
                    for (j4 in -1..1) {
                        manager.setBlock(blockpos2.add(k3.toFloat(), 0f, j4.toFloat()), BLOCK_ACACIA_LEAVES)
                    }
                }
                setLeaves(manager, blockpos2.east().east())
                setLeaves(manager, blockpos2.west().west())
                setLeaves(manager, blockpos2.south().south())
                setLeaves(manager, blockpos2.north().north())
                i3 = position.blockX
                j1 = position.blockZ
                val face1: BlockFace =
                    BlockFace.horizontal[random.nextInt(BlockFace.horizontal.size)]
                if (face1 != blockFace) {
                    val l3 = k2 - random.nextInt(2) - 1
                    var k4 = 1 + random.nextInt(3)
                    k1 = 0
                    var l4 = l3
                    while (l4 < i && k4 > 0) {
                        if (l4 >= 1) {
                            val j2 = position.blockY + l4
                            i3 += face1.offset.blockX
                            j1 += face1.offset.blockZ
                            val blockpos1 = Vector(i3, j2, j1)
                            val material1 = manager.getBlock(blockpos1).type
                            if (material1 == BlockType.AIR || material1 == BlockType.LEAVES2) {
                                manager.setBlock(blockpos1, BLOCK_ACACIA_LOG)
                                k1 = j2
                            }
                        }
                        ++l4
                        --k4
                    }
                    if (k1 > 0) {
                        var blockpos3 = Vector(i3, k1, j1)
                        for (i5 in -2..2) {
                            for (k5 in -2..2) {
                                if (abs(i5) != 2 || abs(k5) != 2) {
                                    setLeaves(manager, blockpos3.add(i5.toFloat(), 0f, k5.toFloat()))
                                }
                            }
                        }
                        blockpos3 = blockpos3.up()
                        for (j5 in -1..1) {
                            for (l5 in -1..1) {
                                setLeaves(manager, blockpos3.add(j5.toFloat(), 0f, l5.toFloat()))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setLeaves(manager: PopulationChunkManager, position: Vector) {
        val type = manager.getBlock(position).type
        if (type == BlockType.AIR || type == BlockType.LEAVES2) {
            manager.setBlock(position, BLOCK_ACACIA_LEAVES)
        }
    }
}
