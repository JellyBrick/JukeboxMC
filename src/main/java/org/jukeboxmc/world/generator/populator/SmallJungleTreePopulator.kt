package org.jukeboxmc.world.generator.populator

import org.jukeboxmc.block.BlockType
import org.jukeboxmc.math.Vector
import org.jukeboxmc.world.World
import org.jukeboxmc.world.chunk.Chunk
import org.jukeboxmc.world.chunk.manager.PopulationChunkManager
import org.jukeboxmc.world.generator.thing.tree.SmallJungleTree
import java.util.Random

/**
 * @author LucGamesYT
 * @version 1.0
 */
class SmallJungleTreePopulator : Populator() {
    private var randomAmount = 0
    private var baseAmount = 0
    override fun populate(
        random: Random,
        world: World?,
        chunkManager: PopulationChunkManager,
        chunkX: Int,
        chunkZ: Int,
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
            SmallJungleTree().create(random, chunkManager, Vector(x, y, z))
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
}
