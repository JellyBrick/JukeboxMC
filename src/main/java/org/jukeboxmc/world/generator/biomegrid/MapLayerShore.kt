package org.jukeboxmc.world.generator.biomegrid

import it.unimi.dsi.fastutil.ints.Int2IntMap
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import it.unimi.dsi.fastutil.ints.IntSet
import org.jukeboxmc.world.Biome

class MapLayerShore(seed: Long, private val belowLayer: MapLayer) : MapLayer(seed) {
    override fun generateValues(x: Int, z: Int, sizeX: Int, sizeZ: Int): IntArray {
        val gridX = x - 1
        val gridZ = z - 1
        val gridSizeX = sizeX + 2
        val gridSizeZ = sizeZ + 2
        val values = belowLayer.generateValues(gridX, gridZ, gridSizeX, gridSizeZ)
        val finalValues = IntArray(sizeX * sizeZ)
        for (i in 0 until sizeZ) {
            for (j in 0 until sizeX) {
                // This applies shores using Von Neumann neighborhood
                // it takes a 3x3 grid with a cross shape and analyzes values as follow
                // OXO
                // XxX
                // OXO
                // the grid center value decides how we are proceeding:
                // - if it's not ocean and it's surrounded by at least 1 ocean cell it turns the center value into beach.
                val upperVal = values!![j + 1 + i * gridSizeX]
                val lowerVal = values[j + 1 + (i + 2) * gridSizeX]
                val leftVal = values[j + (i + 1) * gridSizeX]
                val rightVal = values[j + 2 + (i + 1) * gridSizeX]
                val centerVal = values[j + 1 + (i + 1) * gridSizeX]
                if (!OCEANS.contains(centerVal) && (OCEANS.contains(upperVal) || OCEANS.contains(lowerVal) || OCEANS.contains(
                        leftVal
                    ) || OCEANS.contains(rightVal))
                ) {
                    finalValues[j + i * sizeX] =
                        if (SPECIAL_SHORES.containsKey(centerVal)) SPECIAL_SHORES[centerVal] else Biome.BEACH.id
                } else {
                    finalValues[j + i * sizeX] = centerVal
                }
            }
        }
        return finalValues
    }

    companion object {
        private val OCEANS: IntSet = IntOpenHashSet()
        private val SPECIAL_SHORES: Int2IntMap = Int2IntOpenHashMap()

        init {
            OCEANS.add(Biome.OCEAN.id)
            OCEANS.add(Biome.DEEP_OCEAN.id)
            SPECIAL_SHORES.put(Biome.EXTREME_HILLS.id, Biome.STONE_BEACH.id)
            SPECIAL_SHORES.put(Biome.EXTREME_HILLS_PLUS_TREES.id, Biome.STONE_BEACH.id)
            SPECIAL_SHORES.put(Biome.EXTREME_HILLS_MUTATED.id, Biome.STONE_BEACH.id)
            SPECIAL_SHORES.put(Biome.EXTREME_HILLS_PLUS_TREES_MUTATED.id, Biome.STONE_BEACH.id)
            SPECIAL_SHORES.put(Biome.ICE_PLAINS.id, Biome.COLD_BEACH.id)
            //SPECIAL_SHORES.put(Biome.ICE_MOUNTAINS.id, Biome.COLD_BEACH.id);
            SPECIAL_SHORES.put(Biome.ICE_PLAINS_SPIKES.id, Biome.COLD_BEACH.id)
            SPECIAL_SHORES.put(Biome.COLD_TAIGA.id, Biome.COLD_BEACH.id)
            SPECIAL_SHORES.put(Biome.COLD_TAIGA_HILLS.id, Biome.COLD_BEACH.id)
            SPECIAL_SHORES.put(Biome.COLD_TAIGA_MUTATED.id, Biome.COLD_BEACH.id)
            SPECIAL_SHORES.put(Biome.MUSHROOM_ISLAND.id, Biome.MUSHROOM_ISLAND_SHORE.id)
            SPECIAL_SHORES.put(Biome.SWAMPLAND.id, Biome.SWAMPLAND.id)
            //SPECIAL_SHORES.put( Biome.MESA.getId(), Biome.MESA.getId() );
            //SPECIAL_SHORES.put( Biome.MESA_PLATEAU_STONE.getId(), Biome.MESA_PLATEAU_STONE.getId() );
            //SPECIAL_SHORES.put( Biome.MESA_PLATEAU_STONE_MUTATED.getId(), Biome.MESA_PLATEAU_STONE_MUTATED.getId() );
            //SPECIAL_SHORES.put( Biome.MESA_PLATEAU.getId(), Biome.MESA_PLATEAU.getId() );
            //SPECIAL_SHORES.put( Biome.MESA_PLATEAU_MUTATED.getId(), Biome.MESA_PLATEAU_MUTATED.getId() );
            //SPECIAL_SHORES.put( Biome.MESA_BRYCE.getId(), Biome.MESA_BRYCE.getId() );
        }
    }
}