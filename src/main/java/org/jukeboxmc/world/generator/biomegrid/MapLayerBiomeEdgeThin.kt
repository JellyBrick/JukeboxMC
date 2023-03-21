package org.jukeboxmc.world.generator.biomegrid

import com.google.common.collect.Maps
import it.unimi.dsi.fastutil.ints.Int2IntMap
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.ints.IntList
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import it.unimi.dsi.fastutil.ints.IntSet
import org.jukeboxmc.world.Biome

class MapLayerBiomeEdgeThin(seed: Long, private val belowLayer: MapLayer) : MapLayer(seed) {
    override fun generateValues(x: Int, z: Int, sizeX: Int, sizeZ: Int): IntArray {
        val gridX = x - 1
        val gridZ = z - 1
        val gridSizeX = sizeX + 2
        val gridSizeZ = sizeZ + 2
        val values = belowLayer.generateValues(gridX, gridZ, gridSizeX, gridSizeZ)
        val finalValues = IntArray(sizeX * sizeZ)
        for (i in 0 until sizeZ) {
            for (j in 0 until sizeX) {
                // This applies biome thin edges using Von Neumann neighborhood
                val centerVal = values!![j + 1 + (i + 1) * gridSizeX]
                var `val` = centerVal
                for ((map, entryValue) in EDGES) {
                    if (map.containsKey(centerVal)) {
                        val upperVal = values[j + 1 + i * gridSizeX]
                        val lowerVal = values[j + 1 + (i + 2) * gridSizeX]
                        val leftVal = values[j + (i + 1) * gridSizeX]
                        val rightVal = values[j + 2 + (i + 1) * gridSizeX]
                        if (entryValue == null && (
                                !OCEANS.contains(upperVal) && !map.containsKey(upperVal) || !OCEANS.contains(
                                    lowerVal,
                                ) && !map.containsKey(lowerVal) || !OCEANS.contains(leftVal) && !map.containsKey(leftVal) || !OCEANS.contains(
                                    rightVal,
                                ) && !map.containsKey(rightVal)
                                )
                        ) {
                            `val` = map[centerVal]
                            break
                        } else if (entryValue != null && (
                                !OCEANS.contains(upperVal) && !entryValue.contains(upperVal) || !OCEANS.contains(
                                    lowerVal,
                                ) && !entryValue.contains(lowerVal) || !OCEANS.contains(leftVal) && !entryValue.contains(
                                    leftVal,
                                ) || !OCEANS.contains(rightVal) && !entryValue.contains(rightVal)
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
        private val OCEANS: IntSet = IntOpenHashSet()
        private val MESA_EDGES: Int2IntMap = Int2IntOpenHashMap()
        private val JUNGLE_EDGES: Int2IntMap = Int2IntOpenHashMap()
        private val EDGES: MutableMap<Int2IntMap, IntList?> = Maps.newHashMap()

        init {
            OCEANS.add(Biome.OCEAN.id)
            OCEANS.add(Biome.DEEP_OCEAN.id)

            // MESA_EDGES.put( Biome.MESA.getId(), Biome.DESERT.getId() );
            // MESA_EDGES.put( Biome.MESA_BRYCE.getId(), Biome.DESERT.getId() );
            // MESA_EDGES.put( Biome.MESA_PLATEAU_STONE.getId(), Biome.DESERT.getId() );
            // .put( Biome.MESA_PLATEAU_STONE_MUTATED.getId(), Biome.DESERT.getId() );
            // MESA_EDGES.put( Biome.MESA_PLATEAU.getId(), Biome.DESERT.getId() );
            // MESA_EDGES.put( Biome.MESA_PLATEAU_MUTATED.getId(), Biome.DESERT.getId() );
            JUNGLE_EDGES.put(Biome.JUNGLE.id, Biome.JUNGLE_EDGE.id)
            JUNGLE_EDGES.put(Biome.JUNGLE_HILLS.id, Biome.JUNGLE_EDGE.id)
            JUNGLE_EDGES.put(Biome.JUNGLE_MUTATED.id, Biome.JUNGLE_EDGE.id)
            JUNGLE_EDGES.put(Biome.JUNGLE_EDGE_MUTATED.id, Biome.JUNGLE_EDGE.id)

            // EDGES.put( MESA_EDGES, null );
            EDGES[JUNGLE_EDGES] = IntArrayList.wrap(
                intArrayOf(
                    Biome.JUNGLE.id,
                    Biome.JUNGLE_HILLS.id,
                    Biome.JUNGLE_MUTATED.id,
                    Biome.JUNGLE_EDGE_MUTATED.id,
                    Biome.FOREST.id,
                    Biome.TAIGA.id,
                ),
            )
        }
    }
}
