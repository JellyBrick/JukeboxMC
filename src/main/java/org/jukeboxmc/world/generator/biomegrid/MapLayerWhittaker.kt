package org.jukeboxmc.world.generator.biomegrid

import com.google.common.collect.Maps

class MapLayerWhittaker
/**
 * Creates a map layer.
 *
 * @param seed the layer random seed
 * @param belowLayer the layer generated before this one
 * @param type the climate-type parameter
 */(seed: Long, private val belowLayer: MapLayer, private val type: ClimateType) : MapLayer(seed) {
    override fun generateValues(x: Int, z: Int, sizeX: Int, sizeZ: Int): IntArray {
        return if (type == ClimateType.WARM_WET || type == ClimateType.COLD_DRY) {
            swapValues(x, z, sizeX, sizeZ)
        } else {
            modifyValues(x, z, sizeX, sizeZ)
        }
    }

    private fun swapValues(x: Int, z: Int, sizeX: Int, sizeZ: Int): IntArray {
        val gridX = x - 1
        val gridZ = z - 1
        val gridSizeX = sizeX + 2
        val gridSizeZ = sizeZ + 2
        val values = belowLayer.generateValues(gridX, gridZ, gridSizeX, gridSizeZ)
        val climate = MAP[type]
        val finalValues = IntArray(sizeX * sizeZ)
        for (i in 0 until sizeZ) {
            for (j in 0 until sizeX) {
                var centerVal = values!![j + 1 + (i + 1) * gridSizeX]
                if (centerVal == climate!!.value) {
                    val upperVal = values[j + 1 + i * gridSizeX]
                    val lowerVal = values[j + 1 + (i + 2) * gridSizeX]
                    val leftVal = values[j + (i + 1) * gridSizeX]
                    val rightVal = values[j + 2 + (i + 1) * gridSizeX]
                    for (type in climate.crossTypes) {
                        if (upperVal == type || lowerVal == type || leftVal == type || rightVal == type) {
                            centerVal = climate.finalValue
                            break
                        }
                    }
                }
                finalValues[j + i * sizeX] = centerVal
            }
        }
        return finalValues
    }

    private fun modifyValues(x: Int, z: Int, sizeX: Int, sizeZ: Int): IntArray {
        val values = belowLayer.generateValues(x, z, sizeX, sizeZ)
        val finalValues = IntArray(sizeX * sizeZ)
        for (i in 0 until sizeZ) {
            for (j in 0 until sizeX) {
                var `val` = values!![j + i * sizeX]
                if (`val` != 0) {
                    setCoordsSeed(x + j, z + i)
                    if (nextInt(13) == 0) {
                        `val` += 1000
                    }
                }
                finalValues[j + i * sizeX] = `val`
            }
        }
        return finalValues
    }

    enum class ClimateType {
        WARM_WET, COLD_DRY, LARGER_BIOMES
    }

    private class Climate(val value: Int, val crossTypes: IntArray, val finalValue: Int)
    companion object {
        private val MAP: MutableMap<ClimateType, Climate> = Maps.newEnumMap(
            ClimateType::class.java,
        )

        init {
            MAP[ClimateType.WARM_WET] = Climate(2, intArrayOf(3, 1), 4)
            MAP[ClimateType.COLD_DRY] = Climate(3, intArrayOf(2, 4), 1)
        }
    }
}
