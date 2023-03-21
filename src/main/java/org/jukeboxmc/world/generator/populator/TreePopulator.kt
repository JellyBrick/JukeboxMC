package org.jukeboxmc.world.generator.populator

import java.util.Random
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.world.World
import org.jukeboxmc.world.chunk.Chunk
import org.jukeboxmc.world.chunk.manager.PopulationChunkManager
import org.jukeboxmc.world.generator.`object`.tree.Tree

/**
 * @author LucGamesYT
 * @version 1.0
 */
class TreePopulator : Populator {
    private var randomAmount = 0
    private var baseAmount = 0
    private val treeType: Tree.TreeType
    private val mutated: Boolean

    constructor(treeType: Tree.TreeType) {
        this.treeType = treeType
        mutated = false
    }

    constructor(treeType: Tree.TreeType, mutated: Boolean) {
        this.treeType = treeType
        this.mutated = mutated
    }

    override fun populate(
        random: Random,
        world: World?,
        chunkManager: PopulationChunkManager,
        chunkX: Int,
        chunkZ: Int
    ) {
        val chunk = chunkManager.getChunk(chunkX, chunkZ)
        val amount = random.nextInt(randomAmount + 1) + baseAmount
        for (i in 0 until amount) {
            val x = random.nextInt(chunkX shl 4, (chunkX shl 4) + 15)
            val z = random.nextInt(chunkZ shl 4, (chunkZ shl 4) + 15)
            val y = getHighestWorkableBlock(chunk!!, x, z)
            if (y == -1) {
                continue
            }
            if (!mutated) {
                Tree(random.nextInt(4) + 5).create(random, chunkManager, x, y, z, treeType)
            } else {
                Tree(random.nextInt(3) + 10).create(random, chunkManager, x, y, z, treeType)
            }
        }
    }

    override fun getHighestWorkableBlock(chunk: Chunk, x: Int, z: Int): Int {
        var y: Int
        y = 127
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
}