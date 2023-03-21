package org.jukeboxmc.world.generator.biome.generation

import java.util.Random
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockDirt
import org.jukeboxmc.block.data.DirtType
import org.jukeboxmc.world.chunk.Chunk
import org.jukeboxmc.world.generator.biome.GroundGenerator

/**
 * @author LucGamesYT
 * @version 1.0
 */
class GroundGeneratorPatchDirtAndStone : GroundGenerator() {
    override fun generateTerrainColumn(chunk: Chunk, random: Random, chunkX: Int, chunkZ: Int, surfaceNoise: Double) {
        if (surfaceNoise > 1.75) {
            topMaterial = Block.Companion.create<Block>(BlockType.STONE)
            groundMaterial = Block.Companion.create<Block>(BlockType.STONE)
        } else if (surfaceNoise > -0.5) {
            topMaterial = Block.Companion.create<BlockDirt>(BlockType.DIRT).setDirtType(DirtType.COARSE)
            groundMaterial = Block.Companion.create<Block>(BlockType.STONE)
        } else {
            topMaterial = Block.Companion.create<Block>(BlockType.GRASS)
            groundMaterial = Block.Companion.create<Block>(BlockType.DIRT)
        }
        super.generateTerrainColumn(chunk, random, chunkX, chunkZ, surfaceNoise)
    }
}