package org.jukeboxmc.world.generator.`object`.tree

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockLeaves
import org.jukeboxmc.block.behavior.BlockLog
import org.jukeboxmc.block.behavior.BlockVine
import org.jukeboxmc.block.data.LeafType
import org.jukeboxmc.block.data.LogType
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.math.Vector
import org.jukeboxmc.world.chunk.manager.PopulationChunkManager
import java.util.Random

/**
 * @author LucGamesYT
 * @version 1.0
 */
class SmallJungleTree {
    private val BLOCK_LOG: Block = Block.create<BlockLog>(BlockType.LOG).setLogType(LogType.JUNGLE)
    private val BLOCK_LEAVES: Block = Block.create<BlockLeaves>(BlockType.LEAVES).setLeafType(LeafType.JUNGLE)
    private val BLOCK_VINE: Block = Block.create<BlockVine>(BlockType.VINE)
    private val BLOCK_DIRT: Block = Block.create<Block>(BlockType.DIRT)
    fun create(random: Random, manager: PopulationChunkManager, position: Vector) {
        val i = random.nextInt(4 + random.nextInt(7)) + 3
        var flag = true
        if (position.getY() >= 1 && position.getY() + i + 1 <= 256) {
            var j = position.blockY
            while (j <= position.getY() + 1 + i) {
                var k = 1
                if (j.toFloat() == position.getY()) {
                    k = 0
                }
                if (j >= position.getY() + 1 + i - 2) {
                    k = 2
                }
                val pos2 = Vector(0, 0, 0)
                var l = position.blockZ - k
                while (l <= position.getX() + k && flag) {
                    var i1 = position.blockZ - k
                    while (i1 <= position.getZ() + k && flag) {
                        if (j >= 0 && j < 256) {
                            pos2.setVector(l, j, i1)
                            if (!canPlace(manager.getBlock(pos2).type)) {
                                flag = false
                            }
                        } else {
                            flag = false
                        }
                        ++i1
                    }
                    ++l
                }
                ++j
            }
            if (flag) {
                val down = position.down()
                val block = manager.getBlock(down).type
                if ((block == BlockType.GRASS || block == BlockType.DIRT || block == BlockType.FARMLAND) && position.getY() < 256 - i - 1) {
                    manager.setBlock(down, BLOCK_DIRT)
                    var i3 = position.blockY - 3 + i
                    while (i3 <= position.getY() + i) {
                        val i4 = i3 - (position.blockY + i)
                        val j1 = 1 - i4 / 2
                        var k1 = position.blockX - j1
                        while (k1 <= position.getX() + j1) {
                            val l1 = k1 - position.blockX
                            var i2 = position.blockZ - j1
                            while (i2 <= position.getZ() + j1) {
                                val j2 = i2 - position.blockZ
                                if (Math.abs(l1) != j1 || Math.abs(j2) != j1 || random.nextInt(2) != 0 && i4 != 0) {
                                    val blockpos = Vector(k1, i3, i2)
                                    val id = manager.getBlock(blockpos).type
                                    if (id == BlockType.AIR || id == BlockType.LEAVES || id == BlockType.VINE) {
                                        manager.setBlock(blockpos, BLOCK_LEAVES)
                                    }
                                }
                                ++i2
                            }
                            ++k1
                        }
                        ++i3
                    }
                    for (j3 in 0 until i) {
                        val up = position.add(0f, j3.toFloat(), 0f)
                        val id = manager.getBlock(up!!).type
                        if (id == BlockType.AIR || id == BlockType.LEAVES || id == BlockType.VINE) {
                            manager.setBlock(up, BLOCK_LOG)
                            if (j3 > 0) {
                                if (random.nextInt(3) > 0 && isAirBlock(manager, position.add(-1f, j3.toFloat(), 0f))) {
                                    this.addVine(manager, position.add(-1f, j3.toFloat(), 0f), 8)
                                }
                                if (random.nextInt(3) > 0 && isAirBlock(manager, position.add(1f, j3.toFloat(), 0f))) {
                                    this.addVine(manager, position.add(1f, j3.toFloat(), 0f), 2)
                                }
                                if (random.nextInt(3) > 0 && isAirBlock(manager, position.add(0f, j3.toFloat(), -1f))) {
                                    this.addVine(manager, position.add(0f, j3.toFloat(), -1f), 1)
                                }
                                if (random.nextInt(3) > 0 && isAirBlock(manager, position.add(0f, j3.toFloat(), 1f))) {
                                    this.addVine(manager, position.add(0f, j3.toFloat(), 1f), 4)
                                }
                            }
                        }
                    }
                }
                var k3 = position.blockY - 3 + i
                while (k3 <= position.getY() + i) {
                    val j4 = k3 - (position.blockY + i)
                    val k4 = 2 - j4 / 2
                    val pos2 = Vector(0, 0, 0)
                    var l4 = position.blockX - k4
                    while (l4 <= position.getX() + k4) {
                        var i5 = position.blockZ - k4
                        while (i5 <= position.getZ() + k4) {
                            pos2.setVector(l4, k3, i5)
                            if (manager.getBlock(pos2).type == BlockType.LEAVES) {
                                val blockpos2 = pos2.west()
                                val blockpos3 = pos2.east()
                                val blockpos4 = pos2.north()
                                val blockpos1 = pos2.south()
                                if (random.nextInt(4) == 0 && manager.getBlock(blockpos2).type == BlockType.AIR) {
                                    addHangingVine(manager, blockpos2, 8)
                                }
                                if (random.nextInt(4) == 0 && manager.getBlock(blockpos3).type == BlockType.AIR) {
                                    addHangingVine(manager, blockpos3, 2)
                                }
                                if (random.nextInt(4) == 0 && manager.getBlock(blockpos4).type == BlockType.AIR) {
                                    addHangingVine(manager, blockpos4, 1)
                                }
                                if (random.nextInt(4) == 0 && manager.getBlock(blockpos1).type == BlockType.AIR) {
                                    addHangingVine(manager, blockpos1, 4)
                                }
                            }
                            ++i5
                        }
                        ++l4
                    }
                    ++k3
                }
            }
        }
    }

    private fun addHangingVine(manager: PopulationChunkManager, position: Vector, value: Int) {
        var position = position
        this.addVine(manager, position, value)
        var i = 4
        position = position.down()
        while (i > 0 && manager.getBlock(position).type == BlockType.AIR) {
            this.addVine(manager, position, value)
            position = position.down()
            --i
        }
    }

    private fun addVine(manager: PopulationChunkManager, position: Vector, blockFace: BlockFace) {
        manager.setBlock(position, (BLOCK_VINE as BlockVine).setVineDirection(1 shl blockFace.horizontalIndex))
    }

    private fun addVine(manager: PopulationChunkManager, position: Vector, value: Int) {
        manager.setBlock(position, (BLOCK_VINE.clone() as BlockVine).setVineDirection(value))
    }

    private fun canPlace(blockType: BlockType?): Boolean {
        return blockType == BlockType.AIR || blockType == BlockType.LEAVES || blockType == BlockType.GRASS || blockType == BlockType.DIRT || blockType == BlockType.LOG || blockType == BlockType.LOG2 || blockType == BlockType.SAPLING || blockType == BlockType.VINE
    }

    private fun isAirBlock(manager: PopulationChunkManager, position: Vector): Boolean {
        return manager.getBlock(position).type == BlockType.AIR
    }
}
