package org.jukeboxmc.world.generator.biomegrid

import com.google.common.collect.Maps
import it.unimi.dsi.fastutil.ints.Int2IntMap
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.ints.IntList
import org.jukeboxmc.world.Biome

class MapLayerBiomeEdge(seed: Long, private val belowLayer: MapLayer) : MapLayer(seed) {
    override fun generateValues(x: Int, z: Int, sizeX: Int, sizeZ: Int): IntArray {
        val gridX = x - 1
        val gridZ = z - 1
        val gridSizeX = sizeX + 2
        val gridSizeZ = sizeZ + 2
        val values = belowLayer.generateValues(gridX, gridZ, gridSizeX, gridSizeZ)
        val finalValues = IntArray(sizeX * sizeZ)
        for (i in 0 until sizeZ) {
            for (j in 0 until sizeX) {
                // This applies biome large edges using Von Neumann neighborhood
                val centerVal = values!![j + 1 + (i + 1) * gridSizeX]
                var `val` = centerVal
                for ((map, entryValue) in EDGES) {
                    if (map.containsKey(centerVal)) {
                        val upperVal = values[j + 1 + i * gridSizeX]
                        val lowerVal = values[j + 1 + (i + 2) * gridSizeX]
                        val leftVal = values[j + (i + 1) * gridSizeX]
                        val rightVal = values[j + 2 + (i + 1) * gridSizeX]
                        if (entryValue == null && (
                                !map.containsKey(upperVal) || !map.containsKey(lowerVal) || !map.containsKey(
                                    leftVal,
                                ) || !map.containsKey(rightVal)
                                )
                        ) {
                            `val` = map[centerVal]
                            break
                        } else if (entryValue != null && (
                                entryValue.contains(upperVal) || entryValue.contains(lowerVal) || entryValue.contains(
                                    leftVal,
                                ) || entryValue.contains(rightVal)
                                )
                        ) {
                            `val` = map[centerVal]
                            break
                        }
                    }
                }
                finalValues[j + i * sizeX] = `val`
            }
        }
        return finalValues
    }

    companion object {
        private val MESA_EDGES: Int2IntMap = Int2IntOpenHashMap()
        private val MEGA_TAIGA_EDGES: Int2IntMap = Int2IntOpenHashMap()
        private val DESERT_EDGES: Int2IntMap = Int2IntOpenHashMap()
        private val SWAMP1_EDGES: Int2IntMap = Int2IntOpenHashMap()
        private val SWAMP2_EDGES: Int2IntMap = Int2IntOpenHashMap()
        private val EDGES: MutableMap<Int2IntMap, IntList?> = Maps.newHashMap()

        init {
            // MESA_EDGES.put( Biome.MESA_PLATEAU_STONE.getId(), Biome.MESA.getId() );
            // MESA_EDGES.put( Biome.MESA_PLATEAU.getId(), Biome.MESA.getId() );
            MEGA_TAIGA_EDGES.put(Biome.MEGA_TAIGA.id, Biome.TAIGA.id)
            DESERT_EDGES.put(Biome.DESERT.id, Biome.EXTREME_HILLS_PLUS_TREES.id)
            SWAMP1_EDGES.put(Biome.SWAMPLAND.id, Biome.PLAINS.id)
            SWAMP2_EDGES.put(Biome.SWAMPLAND.id, Biome.JUNGLE_EDGE.id)

            // EDGES.put( MESA_EDGES, null );
            EDGES[MEGA_TAIGA_EDGES] = null
            EDGES[DESERT_EDGES] = IntArrayList.wrap(intArrayOf(Biome.ICE_PLAINS.id))
            EDGES[SWAMP1_EDGES] =
                IntArrayList.wrap(
                    intArrayOf(
                        Biome.DESERT.id,
                        Biome.COLD_TAIGA.id,
                        Biome.ICE_PLAINS.id,
                    ),
                )
            EDGES[SWAMP2_EDGES] =
                IntArrayList.wrap(intArrayOf(Biome.JUNGLE.id))
        }
    }
}
