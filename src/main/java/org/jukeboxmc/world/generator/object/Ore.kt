package org.jukeboxmc.world.generator.`object`

import java.util.Random
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.world.chunk.manager.PopulationChunkManager

/**
 * @author LucGamesYT
 * @version 1.0
 */
class Ore(private val random: Random, private val oreType: OreType) {
    fun canPlace(manager: PopulationChunkManager, x: Int, y: Int, z: Int): Boolean {
        return manager.getBlock(x, y, z).type == BlockType.STONE
    }

    fun place(manager: PopulationChunkManager, x: Int, y: Int, z: Int) {
        val piScaled = random.nextFloat() * Math.PI.toFloat()
        val scaleMaxX = (x + 8).toFloat() + Math.sin(piScaled.toDouble()) * oreType.clusterSize.toFloat() / 8.0f
        val scaleMinX = (x + 8).toFloat() - Math.sin(piScaled.toDouble()) * oreType.clusterSize.toFloat() / 8.0f
        val scaleMaxZ = (z + 8).toFloat() + Math.cos(piScaled.toDouble()) * oreType.clusterSize.toFloat() / 8.0f
        val scaleMinZ = (z + 8).toFloat() - Math.cos(piScaled.toDouble()) * oreType.clusterSize.toFloat() / 8.0f
        val scaleMaxY = (y + random.nextInt(3) - 2).toDouble()
        val scaleMinY = (y + random.nextInt(3) - 2).toDouble()
        for (i in 0 until oreType.clusterSize) {
            val sizeIncr = i.toFloat() / oreType.clusterSize.toFloat()
            val scaleX = scaleMaxX + (scaleMinX - scaleMaxX) * sizeIncr.toDouble()
            val scaleY = scaleMaxY + (scaleMinY - scaleMaxY) * sizeIncr.toDouble()
            val scaleZ = scaleMaxZ + (scaleMinZ - scaleMaxZ) * sizeIncr.toDouble()
            val randSizeOffset = random.nextDouble() * oreType.clusterSize.toDouble() / 16.0
            val randVec1 = (Math.sin((Math.PI.toFloat() * sizeIncr).toDouble()) + 1.0f) * randSizeOffset + 1.0
            val randVec2 = (Math.sin((Math.PI.toFloat() * sizeIncr).toDouble()) + 1.0f) * randSizeOffset + 1.0
            val minX = Math.floor(scaleX - randVec1 / 2.0).toInt()
            val minY = Math.floor(scaleY - randVec2 / 2.0).toInt()
            val minZ = Math.floor(scaleZ - randVec1 / 2.0).toInt()
            val maxX = Math.floor(scaleX + randVec1 / 2.0).toInt()
            val maxY = Math.floor(scaleY + randVec2 / 2.0).toInt()
            val maxZ = Math.floor(scaleZ + randVec1 / 2.0).toInt()
            for (xSeg in minX..maxX) {
                val xVal = (xSeg.toDouble() + 0.5 - scaleX) / (randVec1 / 2.0)
                if (xVal * xVal < 1.0) {
                    for (ySeg in minY..maxY) {
                        val yVal = (ySeg.toDouble() + 0.5 - scaleY) / (randVec2 / 2.0)
                        if (xVal * xVal + yVal * yVal < 1.0) {
                            for (zSeg in minZ..maxZ) {
                                val zVal = (zSeg.toDouble() + 0.5 - scaleZ) / (randVec1 / 2.0)
                                if (xVal * xVal + yVal * yVal + zVal * zVal < 1.0) {
                                    if (canPlace(manager, xSeg, ySeg, zSeg)) {
                                        manager.setBlock(xSeg, ySeg, zSeg, oreType.block)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}