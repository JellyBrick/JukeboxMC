package org.jukeboxmc.world.generator.biome.generation

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockDirt
import org.jukeboxmc.block.data.DirtType
import org.jukeboxmc.world.chunk.Chunk
import org.jukeboxmc.world.generator.biome.GroundGenerator
import java.util.Random

/**
 * @author LucGamesYT
 * @version 1.0
 */
class GroundGeneratorPatchDirt : GroundGenerator() {
    override fun generateTerrainColumn(chunk: Chunk, random: Random, chunkX: Int, chunkZ: Int, surfaceNoise: Double) {
        topMaterial = if (surfaceNoise > 1.75) {
            Block.create<BlockDirt>(BlockType.DIRT).setDirtType(DirtType.COARSE)
        } else if (surfaceNoise > -0.95) {
            Block.create(BlockType.PODZOL)
        } else {
            Block.create(BlockType.GRASS)
        }
        groundMaterial = Block.create(BlockType.DIRT)
        super.generateTerrainColumn(chunk, random, chunkX, chunkZ, surfaceNoise)
    }
}
