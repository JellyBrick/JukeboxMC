package org.jukeboxmc.world.generator.biome.generation

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.world.generator.biome.GroundGenerator

/**
 * @author LucGamesYT
 * @version 1.0
 */
class GroundGeneratorSandy : GroundGenerator() {
    init {
        topMaterial = Block.Companion.create<Block>(BlockType.SAND)
        groundMaterial = Block.Companion.create<Block>(BlockType.SAND)
    }
}