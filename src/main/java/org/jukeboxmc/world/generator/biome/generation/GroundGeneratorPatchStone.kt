package org.jukeboxmc.world.generator.biome.generation

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.world.chunk.Chunk
import org.jukeboxmc.world.generator.biome.GroundGenerator
import java.util.Random

/**
 * @author LucGamesYT
 * @version 1.0
 */
class GroundGeneratorPatchStone : GroundGenerator() {
    override fun generateTerrainColumn(chunk: Chunk, random: Random, chunkX: Int, chunkZ: Int, surfaceNoise: Double) {
        if (surfaceNoise > 1.0) {
            topMaterial = Block.create<Block>(BlockType.STONE)
            groundMaterial = Block.create<Block>(BlockType.STONE)
        } else {
            topMaterial = Block.create<Block>(BlockType.GRASS)
            groundMaterial = Block.create<Block>(BlockType.DIRT)
        }
        super.generateTerrainColumn(chunk, random, chunkX, chunkZ, surfaceNoise)
    }
}
