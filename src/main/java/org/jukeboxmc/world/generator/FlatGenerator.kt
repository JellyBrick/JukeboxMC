package org.jukeboxmc.world.generator

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.math.Vector
import org.jukeboxmc.world.Biome
import org.jukeboxmc.world.World
import org.jukeboxmc.world.chunk.Chunk
import org.jukeboxmc.world.chunk.manager.PopulationChunkManager

/**
 * @author LucGamesYT
 * @version 1.0
 */
class FlatGenerator(world: World?) : Generator() {
    private val blockGrass: Block = Block.create(BlockType.GRASS)
    private val blockDirt: Block = Block.create(BlockType.DIRT)
    private val blockBedrock: Block

    init {
        blockBedrock = Block.create(BlockType.BEDROCK)
    }

    override fun generate(chunk: Chunk, chunkX: Int, chunkZ: Int) {
        for (blockX in 0..15) {
            for (blockZ in 0..15) {
                for (blockY in 0..15) {
                    chunk.setBiome(blockX, blockY, blockZ, Biome.PLAINS)
                }
                chunk.setBlock(blockX, 0, blockZ, 0, blockBedrock)
                chunk.setBlock(blockX, 1, blockZ, 0, blockDirt)
                chunk.setBlock(blockX, 2, blockZ, 0, blockDirt)
                chunk.setBlock(blockX, 3, blockZ, 0, blockGrass)
            }
        }
    }

    override fun populate(manager: PopulationChunkManager, chunkX: Int, chunkZ: Int) {}
    override fun finish(manager: PopulationChunkManager, chunkX: Int, chunkZ: Int) {}
    override val spawnLocation: Vector
        get() = Vector(0.5f, 5f, 0.5f)
}
