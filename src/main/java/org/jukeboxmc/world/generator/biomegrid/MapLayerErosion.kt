package org.jukeboxmc.world.generator.biomegrid

class MapLayerErosion(seed: Long, private val belowLayer: MapLayer) : MapLayer(seed) {
    override fun generateValues(x: Int, z: Int, sizeX: Int, sizeZ: Int): IntArray {
        val gridX = x - 1
        val gridZ = z - 1
        val gridSizeX = sizeX + 2
        val gridSizeZ = sizeZ + 2
        val values = belowLayer.generateValues(gridX, gridZ, gridSizeX, gridSizeZ)
        val finalValues = IntArray(sizeX * sizeZ)
        for (i in 0 until sizeZ) {
            for (j in 0 until sizeX) {
                // This applies erosion using Rotated Von Neumann neighborhood
                // it takes a 3x3 grid with a cross shape and analyzes values as follow
                // XOX
                // OXO
                // XOX
                // the grid center value decides how we are proceeding:
                // - if it's land and it's surrounded by at least 1 ocean cell there are 4/5 chances to proceed to land weathering, and 1/5 chance to spread some land.
                // - if it's ocean and it's surrounded by at least 1 land cell, there are 2/3 chances to proceed to land weathering, and 1/3 chance to spread some land.
                val upperLeftVal = values!![j + i * gridSizeX]
                val lowerLeftVal = values[j + (i + 2) * gridSizeX]
                val upperRightVal = values[j + 2 + i * gridSizeX]
                val lowerRightVal = values[j + 2 + (i + 2) * gridSizeX]
                val centerVal = values[j + 1 + (i + 1) * gridSizeX]
                setCoordsSeed(x + j, z + i)
                if (centerVal != 0 && (upperLeftVal == 0 || upperRightVal == 0 || lowerLeftVal == 0 || lowerRightVal == 0)) {
                    finalValues[j + i * sizeX] = if (nextInt(5) == 0) 0 else centerVal
                } else if (centerVal == 0 && (upperLeftVal != 0 || upperRightVal != 0 || lowerLeftVal != 0 || lowerRightVal != 0)) {
                    if (nextInt(3) == 0) {
                        finalValues[j + i * sizeX] = upperLeftVal
                    } else {
                        finalValues[j + i * sizeX] = 0
                    }
                } else {
                    finalValues[j + i * sizeX] = centerVal
                }
            }
        }
        return finalValues
    }
}
