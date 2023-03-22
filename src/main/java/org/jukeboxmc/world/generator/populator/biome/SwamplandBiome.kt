package org.jukeboxmc.world.generator.populator.biome

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.world.generator.populator.DiskPopulator
import org.jukeboxmc.world.generator.populator.LilyPadPopulator
import org.jukeboxmc.world.generator.populator.SeagrassPopulator
import org.jukeboxmc.world.generator.populator.SwampTreePopulator
import org.jukeboxmc.world.generator.populator.TallGrassPopulator
import org.jukeboxmc.world.generator.populator.YellowFlowerPopulator

/**
 * @author LucGamesYT
 * @version 1.0
 */
class SwamplandBiome : BiomePopulator() {
    init {
        val diskPopulator = DiskPopulator(
            1.0,
            Block.create(BlockType.CLAY),
            1,
            2,
            1,
            listOf(
                BlockType.DIRT,
                BlockType.CLAY,
            ),
        )
        diskPopulator.setBaseAmount(1)
        addPopulator(diskPopulator)
        val lilyPadPopulator = LilyPadPopulator()
        lilyPadPopulator.setBaseAmount(4)
        lilyPadPopulator.setRandomAmount(2)
        addPopulator(lilyPadPopulator)
        val seagrassPopulator = SeagrassPopulator(0.6)
        seagrassPopulator.setRandomAmount(32)
        seagrassPopulator.setBaseAmount(32)
        addPopulator(seagrassPopulator)
        val swampTreePopulator = SwampTreePopulator()
        swampTreePopulator.setBaseAmount(2)
        addPopulator(swampTreePopulator)
        val tallGrassPopulator = TallGrassPopulator()
        tallGrassPopulator.setBaseAmount(0)
        tallGrassPopulator.setRandomAmount(3)
        addPopulator(tallGrassPopulator)
        val yellowFlowerPopulator = YellowFlowerPopulator()
        yellowFlowerPopulator.setBaseAmount(2)
        addPopulator(yellowFlowerPopulator)

        // TODO MushroomPopulator
    }
}
