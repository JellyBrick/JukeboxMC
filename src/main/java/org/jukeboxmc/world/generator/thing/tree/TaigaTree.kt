package org.jukeboxmc.world.generator.thing.tree

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockLeaves
import org.jukeboxmc.block.behavior.BlockLog
import org.jukeboxmc.block.data.LeafType
import org.jukeboxmc.block.data.LogType
import org.jukeboxmc.world.chunk.manager.PopulationChunkManager
import java.util.Random
import kotlin.math.abs

/**
 * @author LucGamesYT
 * @version 1.0
 */
class TaigaTree(treeHeight: Int) : Tree(treeHeight) {
    private val BLOCK_SPRUCE_LOG: Block = Block.create<BlockLog>(BlockType.LOG).setLogType(LogType.SPRUCE)
    private val BLOCK_SPRUCE_LEAVES: Block = Block.create<BlockLeaves>(BlockType.LEAVES).setLeafType(
        LeafType.SPRUCE,
    )

    fun create(random: Random, manager: PopulationChunkManager, x: Int, y: Int, z: Int) {
        treeHeight -= random.nextInt(3)
        for (i in 0 until treeHeight) {
            manager.setBlock(x, y + i, z, BLOCK_SPRUCE_LOG)
        }
        val topSize = treeHeight - (1 + random.nextInt(2))
        val lRadius = 2 + random.nextInt(2)
        var radius = random.nextInt(2)
        var maxR = 1
        var minR = 0
        for (yy in 0..topSize) {
            val yyy = y + treeHeight - yy
            for (xx in x - radius..x + radius) {
                val xOff = abs(xx - x)
                for (zz in z - radius..z + radius) {
                    val zOff = abs(zz - z)
                    if (xOff == radius && zOff == radius && radius > 0) {
                        continue
                    }
                    manager.setBlock(xx, yyy, zz, BLOCK_SPRUCE_LEAVES)
                }
            }
            if (radius >= maxR) {
                radius = minR
                minR = 1
                if (++maxR > lRadius) {
                    maxR = lRadius
                }
            } else {
                ++radius
            }
        }
    } /*
       manager.setBlock( x, y + 1, z, BLOCK_SPRUCE_LEAVES );
        for ( int xx = -2; xx <= 2; xx++ ) {
            for ( int yy = -2; yy <= 2; yy++ ) {
                for ( int zz = -2; zz <= 2; zz++ ) {
                    if ( Math.abs( xx ) + Math.abs( yy ) + Math.abs( zz ) > 3 ) {
                        manager.setBlock( xx + x, yy + y + 1, zz + z, BLOCK_SPRUCE_LEAVES );
                    }
                }
            }
        }
     */
}
