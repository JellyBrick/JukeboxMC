package org.jukeboxmc.world.generator.populator.biome

import org.jukeboxmc.world.Biome
import java.util.EnumMap

/**
 * @author LucGamesYT
 * @version 1.0
 */
object BiomePopulatorRegistry {
    private val POPULATOR_MAP: MutableMap<Biome, BiomePopulator> = EnumMap(org.jukeboxmc.world.Biome::class.java)
    fun init() {
        register(Biome.RIVER, RiverBiome())
        register(Biome.OCEAN, OceanBiome())
        register(Biome.DEEP_OCEAN, DeepOceanBiome())
        register(Biome.PLAINS, PlainsBiome())
        register(Biome.SUNFLOWER_PLAINS, SunflowerPlainsBiome())

        // register( Biome.ICE_PLAINS, new IcePlainsBiome() );
        // register( Biome.ICE_PLAINS_SPIKES, new IcePlainsSpikesBiome() );
        register(Biome.JUNGLE, JungleBiome())
        register(Biome.JUNGLE_EDGE, JungleBiome())
        register(Biome.JUNGLE_HILLS, JungleBiome())
        register(Biome.JUNGLE_MUTATED, JungleBiome())
        register(Biome.JUNGLE_EDGE_MUTATED, JungleBiome())
        register(Biome.SWAMPLAND, SwamplandBiome())
        register(Biome.SWAMPLAND_MUTATED, SwamplandBiome())
        register(Biome.TAIGA, TaigaBiome())
        register(Biome.TAIGA_MUTATED, TaigaBiome())
        register(Biome.TAIGA_HILLS, TaigaBiome())
        register(Biome.MEGA_TAIGA, MegaTaigaBiome())
        register(Biome.COLD_TAIGA, ColdTaigaBiome())
        register(Biome.COLD_TAIGA_HILLS, ColdTaigaBiome())
        register(Biome.COLD_TAIGA_MUTATED, ColdTaigaBiome())
        register(Biome.FLOWER_FOREST, FlowerForestBiome())
        register(Biome.FOREST, ForestBiome())
        register(Biome.FOREST_HILLS, ForestBiome())
        register(Biome.ROOFED_FOREST, RoofedForestBiome())
        register(Biome.ROOFED_FOREST_MUTATED, RoofedForestBiome())
        register(Biome.BIRCH_FOREST, BirchForestBiome())
        register(Biome.BIRCH_FOREST_MUTATED, BirchForestMutatedBiome())
        register(Biome.BIRCH_FOREST_HILLS, BirchForestBiome())
        register(Biome.BIRCH_FOREST_HILLS_MUTATED, BirchForestHillsMutatedBiome())
        register(Biome.EXTREME_HILLS, ExtremeHillsBiome())
        register(Biome.EXTREME_HILLS_PLUS_TREES, ExtremeHillsBiome())
        register(Biome.SAVANNA, SavannaBiome())
        register(Biome.SAVANNA_PLATEAU, SavannaBiome())
        register(Biome.SAVANNA_MUTATED, SavannaBiome())
        register(Biome.SAVANNA_PLATEAU_MUTATED, SavannaBiome())
        register(Biome.BEACH, BeachBiome())
        register(Biome.COLD_BEACH, ColdBeachBiome())
        register(Biome.DESERT, DesertBiome())
        register(Biome.DESERT_HILLS, DesertBiome())
        register(Biome.DESERT_MUTATED, DesertBiome())
    }

    fun register(biome: Biome, biomePopulator: BiomePopulator) {
        POPULATOR_MAP[biome] = biomePopulator
    }

    fun getBiomePopulator(biome: Biome): BiomePopulator? {
        return POPULATOR_MAP[biome]
    }
}
