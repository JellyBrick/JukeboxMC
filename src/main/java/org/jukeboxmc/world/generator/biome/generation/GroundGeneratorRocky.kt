package org.jukeboxmc.world.generator.biome.generation

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.world.generator.biome.GroundGenerator

/**
 * @author LucGamesYT
 * @version 1.0
 */
class GroundGeneratorRocky : GroundGenerator() {
    init {
        topMaterial = Block.Companion.create<Block>(BlockType.STONE)
        groundMaterial = Block.Companion.create<Block>(BlockType.STONE)
    }
}