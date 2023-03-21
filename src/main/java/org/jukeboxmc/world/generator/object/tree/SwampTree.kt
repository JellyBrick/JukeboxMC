package org.jukeboxmc.world.generator.`object`.tree

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockLeaves
import org.jukeboxmc.block.behavior.BlockLog
import org.jukeboxmc.block.data.LeafType
import org.jukeboxmc.block.data.LogType
import org.jukeboxmc.world.chunk.manager.PopulationChunkManager
import java.util.Random

/**
 * @author LucGamesYT
 * @version 1.0
 */
class SwampTree(treeHeight: Int) : Tree(treeHeight) {
    private val BLOCK_OAK_LEAVES: Block =
        Block.create<BlockLeaves>(BlockType.LEAVES).setLeafType(LeafType.OAK)
    private val BLOCK_OAK_LOG: Block = Block.create<BlockLog>(BlockType.LOG).setLogType(LogType.OAK)
    fun create(random: Random, manager: PopulationChunkManager, x: Int, y: Int, z: Int) {
        for (j in 0 until treeHeight) {
            manager.setBlock(x, y + j, z, BLOCK_OAK_LOG)
        }

        // Create the branches of the tree using blocks of leaves
        for (yy in y - 3 + treeHeight..y + treeHeight) {
            val yOff = (yy - (y + treeHeight)).toDouble()
            val mid = (1 - yOff / 2).toInt() + 1
            for (xx in x - mid..x + mid) {
                val xOff = Math.abs(xx - x)
                for (zz in z - mid..z + mid) {
                    val zOff = Math.abs(zz - z)
                    if (xOff == mid && zOff == mid && (yOff == 0.0 || random.nextInt(3) == 0)) {
                        continue
                    }
                    val block = manager.getBlock(xx, yy, zz, 0)
                    if (!block!!.isSolid) {
                        manager.setBlock(xx, yy, zz, 0, BLOCK_OAK_LEAVES)
                    }
                }
            }
        }
    }
}
