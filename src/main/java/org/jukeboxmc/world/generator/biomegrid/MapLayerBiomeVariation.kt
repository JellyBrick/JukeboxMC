package org.jukeboxmc.world.generator.biomegrid

import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.jukeboxmc.world.Biome

class MapLayerBiomeVariation
/**
 * Creates an instance with no variation layer.
 *
 * @param seed       the PRNG seed
 * @param belowLayer the layer below this one
 * @param variationLayer the variation layer, or null to use no variation layer
 */ @JvmOverloads constructor(
    seed: Long,
    private val belowLayer: MapLayer,
    private val variationLayer: MapLayer? = null,
) : MapLayer(seed) {
    override fun generateValues(x: Int, z: Int, sizeX: Int, sizeZ: Int): IntArray {
        return if (variationLayer == null) {
            generateRandomValues(x, z, sizeX, sizeZ)
        } else {
            mergeValues(x, z, sizeX, sizeZ)
        }
    }

    /**
     * Generates a rectangle, replacing all the positive values in the previous layer with random
     * values from 2 to 31 while leaving zero and negative values unchanged.
     *
     * @param x     the lowest x coordinate
     * @param z     the lowest z coordinate
     * @param sizeX the x coordinate range
     * @param sizeZ the z coordinate range
     * @return a flattened array of generated values
     */
    fun generateRandomValues(x: Int, z: Int, sizeX: Int, sizeZ: Int): IntArray {
        val values = belowLayer.generateValues(x, z, sizeX, sizeZ)
        val finalValues = IntArray(sizeX * sizeZ)
        for (i in 0 until sizeZ) {
            for (j in 0 until sizeX) {
                var value = values[j + i * sizeX]
                if (value > 0) {
                    setCoordsSeed(x + j, z + i)
                    value = nextInt(30) + 2
                }
                finalValues[j + i * sizeX] = value
            }
        }
        return finalValues
    }

    /**
     * Generates a rectangle using the previous layer and the variation layer.
     *
     * @param x     the lowest x coordinate
     * @param z     the lowest z coordinate
     * @param sizeX the x coordinate range
     * @param sizeZ the z coordinate range
     * @return a flattened array of generated values
     */
    fun mergeValues(x: Int, z: Int, sizeX: Int, sizeZ: Int): IntArray {
        val gridX = x - 1
        val gridZ = z - 1
        val gridSizeX = sizeX + 2
        val gridSizeZ = sizeZ + 2
        val values = belowLayer.generateValues(gridX, gridZ, gridSizeX, gridSizeZ)
        val variationValues = variationLayer!!.generateValues(gridX, gridZ, gridSizeX, gridSizeZ)
        val finalValues = IntArray(sizeX * sizeZ)
        for (i in 0 until sizeZ) {
            for (j in 0 until sizeX) {
                setCoordsSeed(x + j, z + i)
                val centerValue = values[j + 1 + (i + 1) * gridSizeX]
                val variationValue = variationValues[j + 1 + (i + 1) * gridSizeX]
                if (centerValue != 0 && variationValue == 3 && centerValue < 128) {
                    finalValues[j + i * sizeX] =
                        if (Biome.findById(centerValue + 128) != null) centerValue + 128 else centerValue
                } else if (variationValue == 2 || nextInt(3) == 0) {
                    var value = centerValue
                    if (VARIATIONS.containsKey(centerValue)) {
                        value = VARIATIONS[centerValue][nextInt(VARIATIONS[centerValue].size)]
                    } else if (centerValue == Biome.DEEP_OCEAN.id && nextInt(3) == 0) {
                        value = ISLANDS[nextInt(ISLANDS.size)]
                    }
                    if (variationValue == 2 && value != centerValue) {
                        value = if (Biome.findById(value + 128) != null) value + 128 else centerValue
                    }
                    if (value != centerValue) {
                        var count = 0
                        if (values[j + 1 + i * gridSizeX] == centerValue) { // upper value
                            count++
                        }
                        if (values[j + 1 + (i + 2) * gridSizeX] == centerValue) { // lower value
                            count++
                        }
                        if (values[j + (i + 1) * gridSizeX] == centerValue) { // left value
                            count++
                        }
                        if (values[j + 2 + (i + 1) * gridSizeX] == centerValue) { // right value
                            count++
                        }
                        // spread mountains if not too close from an edge
                        finalValues[j + i * sizeX] = if (count < 3) centerValue else value
                    } else {
                        finalValues[j + i * sizeX] = value
                    }
                } else {
                    finalValues[j + i * sizeX] = centerValue
                }
            }
        }
        return finalValues
    }

    companion object {
        private val ISLANDS = intArrayOf(Biome.PLAINS.id, Biome.FOREST.id)
        private val VARIATIONS: Int2ObjectMap<IntArray> = Int2ObjectOpenHashMap()

        init {
            VARIATIONS.put(Biome.DESERT.id, intArrayOf(Biome.DESERT_HILLS.id))
            VARIATIONS.put(Biome.FOREST.id, intArrayOf(Biome.FOREST_HILLS.id))
            VARIATIONS.put(Biome.BIRCH_FOREST.id, intArrayOf(Biome.BIRCH_FOREST_HILLS.id))
            VARIATIONS.put(Biome.ROOFED_FOREST.id, intArrayOf(Biome.PLAINS.id))
            VARIATIONS.put(Biome.TAIGA.id, intArrayOf(Biome.TAIGA_HILLS.id))
            VARIATIONS.put(Biome.MEGA_TAIGA.id, intArrayOf(Biome.MEGA_TAIGA_HILLS.id))
            VARIATIONS.put(Biome.COLD_TAIGA.id, intArrayOf(Biome.COLD_TAIGA_HILLS.id))
            VARIATIONS.put(Biome.PLAINS.id, intArrayOf(Biome.FOREST.id, Biome.FOREST.id, Biome.FOREST_HILLS.id))
            // VARIATIONS.put(Biome.ICE_PLAINS.id, new int[]{Biome.ICE_MOUNTAINS.id});
            VARIATIONS.put(Biome.JUNGLE.id, intArrayOf(Biome.JUNGLE_HILLS.id))
            VARIATIONS.put(Biome.OCEAN.id, intArrayOf(Biome.DEEP_OCEAN.id))
            VARIATIONS.put(Biome.EXTREME_HILLS.id, intArrayOf(Biome.EXTREME_HILLS_PLUS_TREES.id))
            VARIATIONS.put(Biome.SAVANNA.id, intArrayOf(Biome.SAVANNA_PLATEAU.id))
            // VARIATIONS.put( Biome.MESA_PLATEAU_STONE.getId(), new int[]{ Biome.MESA.getId() } );
            // VARIATIONS.put( Biome.MESA_PLATEAU.getId(), new int[]{ Biome.MESA.getId() } );
            // VARIATIONS.put( Biome.MESA.getId(), new int[]{ Biome.MESA.getId() } );
        }
    }
}
