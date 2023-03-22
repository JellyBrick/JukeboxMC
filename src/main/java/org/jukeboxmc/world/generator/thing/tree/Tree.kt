package org.jukeboxmc.world.generator.thing.tree

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockLeaves
import org.jukeboxmc.block.behavior.BlockLeaves2
import org.jukeboxmc.block.behavior.BlockLog
import org.jukeboxmc.block.behavior.BlockLog2
import org.jukeboxmc.block.data.LeafType
import org.jukeboxmc.block.data.LeafType2
import org.jukeboxmc.block.data.LogType
import org.jukeboxmc.block.data.LogType2
import org.jukeboxmc.world.chunk.manager.PopulationChunkManager
import java.util.Random

/**
 * @author LucGamesYT
 * @version 1.0
 */
open class Tree(protected var treeHeight: Int) {
    fun create(random: Random, manager: PopulationChunkManager, x: Int, y: Int, z: Int, treeType: TreeType) {
        for (j in 0 until treeHeight) {
            manager.setBlock(x, y + j, z, treeType.blockLog)
        }
        for (yy in y - 3 + treeHeight..y + treeHeight) {
            val yOff = (yy - (y + treeHeight)).toDouble()
            val mid = (1 - yOff / 2).toInt()
            for (xx in x - mid..x + mid) {
                val xOff = Math.abs(xx - x)
                for (zz in z - mid..z + mid) {
                    val zOff = Math.abs(zz - z)
                    if (xOff == mid && zOff == mid && (yOff == 0.0 || random.nextInt(3) == 0)) {
                        continue
                    }
                    val block = manager.getBlock(xx, yy, zz, 0)
                    if (!block!!.isSolid) {
                        manager.setBlock(xx, yy, zz, 0, treeType.blockLeave)
                    }
                }
            }
        }
    }

    enum class TreeType(val blockLog: Block, val blockLeave: Block) {
        OAK(
            Block.create<BlockLog>(BlockType.LOG).setLogType(LogType.OAK),
            Block.create<BlockLeaves>(
                BlockType.LEAVES,
            ).setLeafType(LeafType.OAK),
        ),
        SPRUCE(
            Block.create<BlockLog>(BlockType.LOG).setLogType(
                LogType.SPRUCE,
            ),
            Block.create<BlockLeaves>(BlockType.LEAVES).setLeafType(LeafType.SPRUCE),
        ),
        BIRCH(
            Block.create<BlockLog>(BlockType.LOG).setLogType(LogType.BIRCH),
            Block.create<BlockLeaves>(
                BlockType.LEAVES,
            ).setLeafType(LeafType.BIRCH),
        ),
        JUNGLE(
            Block.create<BlockLog>(BlockType.LOG).setLogType(
                LogType.JUNGLE,
            ),
            Block.create<BlockLeaves>(BlockType.LEAVES).setLeafType(LeafType.JUNGLE),
        ),
        ACACIA(
            Block.create<BlockLog2>(BlockType.LOG2).setLogType(LogType2.ACACIA),
            Block.create<BlockLeaves2>(
                BlockType.LEAVES2,
            ).setLeafType(LeafType2.ACACIA),
        ),
        DARK_OAK(
            Block.create<BlockLog2>(
                BlockType.LOG2,
            ).setLogType(LogType2.DARK_OAK),
            Block.create<BlockLeaves2>(BlockType.LEAVES2).setLeafType(LeafType2.DARK_OAK),
        ),
        MANGROVE(
            Block.create<Block>(BlockType.MANGROVE_LOG),
            Block.create<Block>(BlockType.MANGROVE_LEAVES),
        ),
    }
}
