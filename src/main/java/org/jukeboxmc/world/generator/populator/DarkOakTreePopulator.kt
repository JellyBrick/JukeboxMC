package org.jukeboxmc.world.generator.populator

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockLeaves2
import org.jukeboxmc.block.behavior.BlockLog2
import org.jukeboxmc.block.data.LeafType2
import org.jukeboxmc.block.data.LogType2
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.math.Vector
import org.jukeboxmc.world.World
import org.jukeboxmc.world.chunk.Chunk
import org.jukeboxmc.world.chunk.manager.PopulationChunkManager
import java.util.Random
import kotlin.math.abs

/**
 * @author LucGamesYT
 * @version 1.0
 */
class DarkOakTreePopulator : Populator() {
    private var randomAmount = 0
    private var baseAmount = 0
    private val BLOCK_DARK_OAK_LOG: Block =
        Block.create<BlockLog2>(BlockType.LOG2).setLogType(LogType2.DARK_OAK)
    private val BLOCK_DARK_OAK_LEAVES: Block =
        Block.create<BlockLeaves2>(BlockType.LEAVES2).setLeafType(LeafType2.DARK_OAK)
    private val BLOCK_DIRT: Block = Block.create(BlockType.DIRT)
    override fun populate(
        random: Random,
        world: World?,
        chunkManager: PopulationChunkManager,
        chunkX: Int,
        chunkZ: Int,
    ) {
        val chunk = chunkManager.getChunk(chunkX, chunkZ) ?: return // FIXME
        val amount = random.nextInt(randomAmount + 1) + baseAmount
        for (i in 0 until amount) {
            val x = random.nextInt(chunkX shl 4, (chunkX shl 4) + 15)
            val z = random.nextInt(chunkZ shl 4, (chunkZ shl 4) + 15)
            val y = getHighestWorkableBlock(chunk, x, z)
            if (y == -1) {
                continue
            }
            place(random, chunkManager, Vector(x, y, z))
        }
    }

    override fun getHighestWorkableBlock(chunk: Chunk, x: Int, z: Int): Int {
        var y: Int = 127
        while (y > 0) {
            val blockType = chunk.getBlock(x, y, z, 0).type
            if (blockType == BlockType.DIRT || blockType == BlockType.GRASS) {
                break
            } else if (blockType != BlockType.AIR && blockType != BlockType.SNOW_LAYER) {
                return -1
            }
            --y
        }
        return ++y
    }

    fun setRandomAmount(randomAmount: Int) {
        this.randomAmount = randomAmount
    }

    fun setBaseAmount(baseAmount: Int) {
        this.baseAmount = baseAmount
    }

    private fun place(random: Random, manager: PopulationChunkManager, position: Vector) {
        val treeHeight = random.nextInt(3) + random.nextInt(2) + 6
        val blockX = position.blockX
        val blockY = position.blockY
        val blockZ = position.blockZ
        if (blockY >= 1 && blockY + treeHeight + 1 < 256) {
            val blockpos = position.down()
            var state: BlockType?
            if (!isAnotherTreeNearby(
                    manager,
                    position,
                    treeHeight,
                ) && manager.getBlock(blockpos).type == BlockType.GRASS
            ) {
                setDirtAt(manager, blockpos)
                setDirtAt(manager, blockpos.east())
                setDirtAt(manager, blockpos.south())
                setDirtAt(manager, blockpos.south().east())
                val blockFace: BlockFace =
                    BlockFace.horizontal[random.nextInt(BlockFace.horizontal.size)]
                val i1 = treeHeight - random.nextInt(4)
                var j1 = 2 - random.nextInt(3)
                var x = blockX
                var z = blockZ
                val yy = blockY + treeHeight - 1
                for (k2 in 0 until treeHeight) {
                    if (k2 >= i1 && j1 > 0) {
                        x += blockFace.offset.blockX
                        z += blockFace.offset.blockZ
                        --j1
                    }
                    val y = blockY + k2
                    val blockpos1 = Vector(x, y, z)
                    state = manager.getBlock(blockpos1.blockX, blockpos1.blockY, blockpos1.blockZ).type
                    if (state == BlockType.AIR || state == BlockType.LEAVES || state == BlockType.LEAVES2) {
                        manager.setBlock(blockpos1, BLOCK_DARK_OAK_LOG)
                        manager.setBlock(blockpos1.south(), BLOCK_DARK_OAK_LOG)
                        manager.setBlock(blockpos1.east(), BLOCK_DARK_OAK_LOG)
                        manager.setBlock(blockpos1.south().east(), BLOCK_DARK_OAK_LOG)
                    }
                }
                for (xx in -2..0) {
                    for (zz in -2..0) {
                        var l3 = -1
                        manager.setBlock(x + xx, yy + l3, z + zz, BLOCK_DARK_OAK_LEAVES)
                        manager.setBlock(1 + x - xx, yy + l3, z + zz, BLOCK_DARK_OAK_LEAVES)
                        manager.setBlock(x + xx, yy + l3, 1 + z - zz, BLOCK_DARK_OAK_LEAVES)
                        manager.setBlock(1 + x - xx, yy + l3, 1 + z - zz, BLOCK_DARK_OAK_LEAVES)
                        if ((xx > -2 || zz > -1) && (xx != -1 || zz != -2)) {
                            l3 = 1
                            manager.setBlock(x + xx, yy + l3, z + zz, BLOCK_DARK_OAK_LEAVES)
                            manager.setBlock(1 + x - xx, yy + l3, z + zz, BLOCK_DARK_OAK_LEAVES)
                            manager.setBlock(x + xx, yy + l3, 1 + z - zz, BLOCK_DARK_OAK_LEAVES)
                            manager.setBlock(1 + x - xx, yy + l3, 1 + z - zz, BLOCK_DARK_OAK_LEAVES)
                        }
                    }
                }
                if (random.nextBoolean()) {
                    manager.setBlock(x, yy + 2, z, BLOCK_DARK_OAK_LEAVES)
                    manager.setBlock(x + 1, yy + 2, z, BLOCK_DARK_OAK_LEAVES)
                    manager.setBlock(x + 1, yy + 2, z + 1, BLOCK_DARK_OAK_LEAVES)
                    manager.setBlock(x, yy + 2, z + 1, BLOCK_DARK_OAK_LEAVES)
                }
                for (k3 in -3..4) {
                    for (i3 in -3..4) {
                        if ((k3 != -3 || i3 != -3) && (k3 != -3 || i3 != 4) && (k3 != 4 || i3 != -3) && (k3 != 4 || i3 != 4) && (
                                abs(
                                    k3,
                                ) < 3 || abs(i3) < 3
                                )
                        ) {
                            manager.setBlock(x + k3, yy, z + i3, BLOCK_DARK_OAK_LEAVES)
                        }
                    }
                }
                for (k3 in -1..2) {
                    for (i3 in -1..2) {
                        if ((k3 < 0 || k3 > 1 || i3 < 0 || i3 > 1) && random.nextInt(3) <= 0) {
                            val l3 = random.nextInt(3) + 2
                            for (i4 in 0 until l3) {
                                manager.setBlock(
                                    position.blockX + k3,
                                    yy - i4 - 1,
                                    position.blockZ + i3,
                                    BLOCK_DARK_OAK_LOG,
                                )
                            }
                            for (k4 in -1..1) {
                                for (l4 in -1..1) {
                                    manager.setBlock(
                                        position.blockX + k3 + k4,
                                        yy,
                                        position.blockZ + i3 + l4,
                                        BLOCK_DARK_OAK_LEAVES,
                                    )
                                }
                            }
                            for (k4 in -2..2) {
                                for (l4 in -2..2) {
                                    if (abs(k4) != 2 || abs(l4) != 2) {
                                        manager.setBlock(
                                            position.blockX + k3 + k4,
                                            yy - 1,
                                            position.blockZ + i3 + l4,
                                            BLOCK_DARK_OAK_LEAVES,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun isAnotherTreeNearby(manager: PopulationChunkManager, pos: Vector, height: Int): Boolean {
        val i = pos.blockX
        val j = pos.blockY
        val k = pos.blockZ
        for (l in 0..height + 1) {
            var i1 = 1
            if (l == 0) {
                i1 = 0
            }
            if (l >= height - 1) {
                i1 = 2
            }
            for (j1 in -i1..i1) {
                for (k1 in -i1..i1) {
                    val type = manager.getBlock(i + j1, j + l, k + k1).type
                    if (type == BlockType.LOG || type == BlockType.LOG2) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun setDirtAt(manager: PopulationChunkManager, pos: Vector) {
        if (manager.getBlock(pos.blockX, pos.blockY, pos.blockZ).type != BlockType.DIRT) {
            manager.setBlock(pos.blockX, pos.blockY, pos.blockZ, BLOCK_DIRT)
        }
    }
}
