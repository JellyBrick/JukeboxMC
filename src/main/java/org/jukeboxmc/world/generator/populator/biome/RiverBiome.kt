package org.jukeboxmc.world.generator.populator.biome

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.world.generator.populator.DiskPopulator
import org.jukeboxmc.world.generator.populator.SeagrassPopulator
import org.jukeboxmc.world.generator.populator.SugarcanePopulator
import java.util.Arrays

/**
 * @author LucGamesYT
 * @version 1.0
 */
class RiverBiome : BiomePopulator() {
    init {
        val populatorDiskSand = DiskPopulator(
            1.0,
            Block.create<Block>(BlockType.SAND),
            2,
            4,
            2,
            Arrays.asList(
                BlockType.GRASS,
                BlockType.DIRT,
            ),
        )
        populatorDiskSand.setBaseAmount(3)
        addPopulator(populatorDiskSand)
        val populatorDiskClay = DiskPopulator(
            1.0,
            Block.create<Block>(BlockType.CLAY),
            1,
            2,
            1,
            Arrays.asList(
                BlockType.DIRT,
                BlockType.CLAY,
            ),
        )
        populatorDiskClay.setBaseAmount(1)
        addPopulator(populatorDiskClay)
        val populatorDiskGravel = DiskPopulator(
            1.0,
            Block.create<Block>(BlockType.GRAVEL),
            2,
            3,
            2,
            Arrays.asList(
                BlockType.GRASS,
                BlockType.DIRT,
            ),
        )
        populatorDiskGravel.setBaseAmount(1)
        addPopulator(populatorDiskGravel)
        val seagrassPopulator = SeagrassPopulator(0.4)
        seagrassPopulator.setBaseAmount(24)
        seagrassPopulator.setRandomAmount(24)
        addPopulator(seagrassPopulator)
        val sugarcanePopulator = SugarcanePopulator()
        sugarcanePopulator.setBaseAmount(0)
        sugarcanePopulator.setRandomAmount(20)
        addPopulator(sugarcanePopulator)
    }
}
