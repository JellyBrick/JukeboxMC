package org.jukeboxmc.world.generator.biomegrid

import org.jukeboxmc.world.Biome

class MapLayerDeepOcean(seed: Long, private val belowLayer: MapLayer) : MapLayer(seed) {
    override fun generateValues(x: Int, z: Int, sizeX: Int, sizeZ: Int): IntArray {
        val gridX = x - 1
        val gridZ = z - 1
        val gridSizeX = sizeX + 2
        val gridSizeZ = sizeZ + 2
        val values = belowLayer.generateValues(gridX, gridZ, gridSizeX, gridSizeZ)
        val finalValues = IntArray(sizeX * sizeZ)
        for (i in 0 until sizeZ) {
            for (j in 0 until sizeX) {
                // This applies deep oceans using Von Neumann neighborhood
                // it takes a 3x3 grid with a cross shape and analyzes values as follow
                // OXO
                // XxX
                // OXO
                // the grid center value decides how we are proceeding:
                // - if it's ocean and it's surrounded by 4 ocean cells we spread deep ocean.
                val centerVal = values!![j + 1 + (i + 1) * gridSizeX]
                if (centerVal == 0) {
                    val upperVal = values[j + 1 + i * gridSizeX]
                    val lowerVal = values[j + 1 + (i + 2) * gridSizeX]
                    val leftVal = values[j + (i + 1) * gridSizeX]
                    val rightVal = values[j + 2 + (i + 1) * gridSizeX]
                    if (upperVal == 0 && lowerVal == 0 && leftVal == 0 && rightVal == 0) {
                        setCoordsSeed(x + j, z + i)
                        finalValues[j + i * sizeX] =
                            if (nextInt(100) == 0) Biome.MUSHROOM_ISLAND.id else Biome.DEEP_OCEAN.id
                    } else {
                        finalValues[j + i * sizeX] = centerVal
                    }
                } else {
                    finalValues[j + i * sizeX] = centerVal
                }
            }
        }
        return finalValues
    }
}
