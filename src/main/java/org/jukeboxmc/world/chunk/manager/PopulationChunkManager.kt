package org.jukeboxmc.world.chunk.manager

import com.google.common.base.Preconditions
import org.jukeboxmc.block.Block
import org.jukeboxmc.math.Vector
import org.jukeboxmc.world.chunk.Chunk

/**
 * @author LucGamesYT
 * @version 1.0
 */
class PopulationChunkManager(chunk: Chunk, chunks: List<Chunk>) {
    private val cornerX: Int
    private val cornerZ: Int
    private val chunks = arrayOfNulls<Chunk>(3 * 3)

    init {
        cornerX = chunk.x - 1
        cornerZ = chunk.z - 1
        for (value in chunks) {
            this.chunks[chunkIndex(value.x, value.z)] = value
        }
    }

    private fun chunkIndex(chunkX: Int, chunkZ: Int): Int {
        val relativeX = chunkX - cornerX
        val relativeZ = chunkZ - cornerZ
        Preconditions.checkArgument(
            relativeX >= 0 && relativeX < 3 && relativeZ >= 0 && relativeZ < 3,
            "Chunk position (%s,%s) out of population bounds",
            chunkX,
            chunkZ
        )
        return relativeX * 3 + relativeZ
    }

    private fun chunkFromBlock(blockX: Int, blockZ: Int): Chunk? {
        val relativeX = (blockX shr 4) - cornerX
        val relativeZ = (blockZ shr 4) - cornerZ
        Preconditions.checkArgument(
            relativeX >= 0 && relativeX < 3 && relativeZ >= 0 && relativeZ < 3,
            "Block position (%s,%s) out of population bounds",
            blockX,
            blockZ
        )
        return chunks[relativeX * 3 + relativeZ]
    }

    fun getChunk(chunkX: Int, chunkZ: Int): Chunk? {
        return chunks[chunkIndex(chunkX, chunkZ)]
    }

    fun getBlock(x: Int, y: Int, z: Int, layer: Int): Block {
        return chunkFromBlock(x, z)!!.getBlock(x, y, z, layer)
    }

    fun getBlock(vector: Vector): Block {
        return this.getBlock(vector.blockX, vector.blockY, vector.blockZ, 0)
    }

    fun getBlock(x: Int, y: Int, z: Int): Block {
        return this.getBlock(x, y, z, 0)
    }

    fun setBlock(vector: Vector, layer: Int, block: Block?) {
        chunkFromBlock(vector.blockX, vector.blockZ)!!
            .setBlock(vector.blockX, vector.blockY, vector.blockZ, layer, block)
    }

    fun setBlock(vector: Vector, block: Block?) {
        chunkFromBlock(vector.blockX, vector.blockZ)!!
            .setBlock(vector.blockX, vector.blockY, vector.blockZ, 0, block)
    }

    fun setBlock(x: Int, y: Int, z: Int, layer: Int, block: Block?) {
        chunkFromBlock(x, z)!!.setBlock(x, y, z, layer, block)
    }

    fun setBlock(x: Int, y: Int, z: Int, block: Block?) {
        this.setBlock(x, y, z, 0, block)
    }
}