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
        topMaterial = Block.create(BlockType.SAND)
        groundMaterial = Block.create(BlockType.SAND)
    }
}
