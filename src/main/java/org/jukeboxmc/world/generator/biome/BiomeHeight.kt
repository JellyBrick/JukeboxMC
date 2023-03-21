package org.jukeboxmc.world.generator.biome

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BiomeHeight(height: Double, scale: Double) {
    val height: Double
    val scale: Double

    init {
        this.height = height
        this.scale = scale
    }

    companion object {
        val DEFAULT = BiomeHeight(0.1, 0.3)
        val FLAT_SHORE = BiomeHeight(0.0, 0.025)
        val HIGH_PLATEAU = BiomeHeight(1.5, 0.025)
        val FLATLANDS = BiomeHeight(0.125, 0.05)
        val SWAMPLAND = BiomeHeight(-0.2, 0.1)
        val MID_PLAINS = BiomeHeight(0.2, 0.2)
        val FLATLANDS_HILLS = BiomeHeight(0.275, 0.25)
        val SWAMPLAND_HILLS = BiomeHeight(-0.1, 0.3)
        val LOW_HILLS = BiomeHeight(0.2, 0.3)
        val HILLS = BiomeHeight(0.45, 0.3)
        val MID_HILLS2 = BiomeHeight(0.1, 0.4)
        val DEFAULT_HILLS = BiomeHeight(0.2, 0.4)
        val MID_HILLS = BiomeHeight(0.3, 0.4)
        val BIG_HILLS = BiomeHeight(0.525, 0.55)
        val BIG_HILLS2 = BiomeHeight(0.55, 0.5)
        val EXTREME_HILLS = BiomeHeight(1.0, 0.5)
        val ROCKY_SHORE = BiomeHeight(0.1, 0.8)
        val LOW_SPIKES = BiomeHeight(0.4125, 1.325)
        val HIGH_SPIKES = BiomeHeight(1.1, 1.3125)
        val RIVER = BiomeHeight(-0.5, 0.0)
        val OCEAN = BiomeHeight(-1.0, 0.1)
        val DEEP_OCEAN = BiomeHeight(-1.8, 0.1)
    }
}
