package org.jukeboxmc.world.generator.biomegrid

import it.unimi.dsi.fastutil.ints.Int2IntMap
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import it.unimi.dsi.fastutil.ints.IntSet
import org.jukeboxmc.world.Biome

class MapLayerRiver
/**
 * Creates a map layer that generates rivers.
 *
 * @param seed the layer's PRNG seed
 * @param belowLayer the layer to apply before this one
 * @param mergeLayer
 */ @JvmOverloads constructor(seed: Long, private val belowLayer: MapLayer, private val mergeLayer: MapLayer? = null) :
    MapLayer(seed) {
    override fun generateValues(x: Int, z: Int, sizeX: Int, sizeZ: Int): IntArray {
        return if (mergeLayer == null) {
            generateRivers(x, z, sizeX, sizeZ)
        } else mergeRivers(x, z, sizeX, sizeZ)
    }

    private fun generateRivers(x: Int, z: Int, sizeX: Int, sizeZ: Int): IntArray {
        val gridX = x - 1
        val gridZ = z - 1
        val gridSizeX = sizeX + 2
        val gridSizeZ = sizeZ + 2
        val values = belowLayer.generateValues(gridX, gridZ, gridSizeX, gridSizeZ)
        val finalValues = IntArray(sizeX * sizeZ)
        for (i in 0 until sizeZ) {
            for (j in 0 until sizeX) {
                // This applies rivers using Von Neumann neighborhood
                val centerVal = values!![j + 1 + (i + 1) * gridSizeX] and 1
                val upperVal = values[j + 1 + i * gridSizeX] and 1
                val lowerVal = values[j + 1 + (i + 2) * gridSizeX] and 1
                val leftVal = values[j + (i + 1) * gridSizeX] and 1
                val rightVal = values[j + 2 + (i + 1) * gridSizeX] and 1
                var `val` = CLEAR_VALUE
                if (centerVal != upperVal || centerVal != lowerVal || centerVal != leftVal || centerVal != rightVal) {
                    `val` = RIVER_VALUE
                }
                finalValues[j + i * sizeX] = `val`
            }
        }
        return finalValues
    }

    private fun mergeRivers(x: Int, z: Int, sizeX: Int, sizeZ: Int): IntArray {
        val values = belowLayer.generateValues(x, z, sizeX, sizeZ)
        val mergeValues = mergeLayer!!.generateValues(x, z, sizeX, sizeZ)
        val finalValues = IntArray(sizeX * sizeZ)
        for (i in 0 until sizeX * sizeZ) {
            var `val` = mergeValues!![i]
            if (OCEANS.contains(mergeValues[i])) {
                `val` = mergeValues[i]
            } else if (values!![i] == RIVER_VALUE) {
                `val` = if (SPECIAL_RIVERS.containsKey(mergeValues[i])) {
                    SPECIAL_RIVERS[mergeValues[i]]
                } else {
                    Biome.RIVER.id
                }
            }
            finalValues[i] = `val`
        }
        return finalValues
    }

    companion object {
        private val OCEANS: IntSet = IntOpenHashSet()
        private val SPECIAL_RIVERS: Int2IntMap = Int2IntOpenHashMap()
        private const val CLEAR_VALUE = 0
        private const val RIVER_VALUE = 1

        init {
            OCEANS.add(Biome.OCEAN.id)
            OCEANS.add(Biome.DEEP_OCEAN.id)
            SPECIAL_RIVERS.put(Biome.ICE_PLAINS.id, Biome.FROZEN_RIVER.id)
            SPECIAL_RIVERS.put(Biome.MUSHROOM_ISLAND.id, Biome.MUSHROOM_ISLAND_SHORE.id)
            SPECIAL_RIVERS.put(Biome.MUSHROOM_ISLAND_SHORE.id, Biome.MUSHROOM_ISLAND_SHORE.id)
        }
    }
}