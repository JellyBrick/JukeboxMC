package org.jukeboxmc.world.chunk

import io.netty.buffer.ByteBuf
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.palette.Palette
import org.jukeboxmc.block.palette.RuntimeDataSerializer
import org.jukeboxmc.world.Biome

/**
 * @author LucGamesYT
 * @version 1.0
 */
class SubChunk(val y: Int) {
    var layer = 1
    var subChunkVersion = 9
    val biomes: Palette<Biome> = Palette(Biome.PLAINS)
    val blocks: Array<Palette<Block>> = Array(Chunk.CHUNK_LAYERS) { Palette(Block.create(BlockType.AIR)) }

    init {
        for (layer in 0 until Chunk.CHUNK_LAYERS) {
            blocks[layer] = Palette(BLOCK_AIR)
        }
    }

    fun setBlock(x: Int, y: Int, z: Int, layer: Int, block: Block) {
        blocks[layer][this.indexOf(x, y, z)] = block
    }

    fun getBlock(x: Int, y: Int, z: Int, layer: Int): Block {
        return blocks[layer][this.indexOf(x, y, z)].clone()
    }

    fun setBiome(x: Int, y: Int, z: Int, biome: Biome) {
        biomes[this.indexOf(x, y, z)] = biome
    }

    fun getBiome(x: Int, y: Int, z: Int): Biome {
        return biomes[this.indexOf(x, y, z)]
    }

    private fun indexOf(x: Int, y: Int, z: Int): Int {
        return (x and 15 shl 8) + (z and 15 shl 4) + (y and 15)
    }

    fun writeToNetwork(byteBuf: ByteBuf) {
        byteBuf.writeByte(subChunkVersion)
        byteBuf.writeByte(blocks.size)
        byteBuf.writeByte(y)
        blocks.forEach { blockPalette ->
            blockPalette.writeToNetwork(
                byteBuf,
                object : RuntimeDataSerializer<Block> {
                    override fun serialize(value: Block): Int {
                        return value.runtimeId
                    }
                },
            )
        }
    }

    companion object {
        private val BLOCK_AIR: Block = Block.create(BlockType.AIR)
    }
}
