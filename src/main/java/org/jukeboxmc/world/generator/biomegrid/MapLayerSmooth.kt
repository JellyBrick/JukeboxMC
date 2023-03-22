package org.jukeboxmc.world.generator.biomegrid

class MapLayerSmooth(seed: Long, private val belowLayer: MapLayer) : MapLayer(seed) {
    override fun generateValues(x: Int, z: Int, sizeX: Int, sizeZ: Int): IntArray {
        val gridX = x - 1
        val gridZ = z - 1
        val gridSizeX = sizeX + 2
        val gridSizeZ = sizeZ + 2
        val values = belowLayer.generateValues(gridX, gridZ, gridSizeX, gridSizeZ)
        val finalValues = IntArray(sizeX * sizeZ)
        for (i in 0 until sizeZ) {
            for (j in 0 until sizeX) {
                // This applies smoothing using Von Neumann neighborhood
                // it takes a 3x3 grid with a cross shape and analyzes values as follows
                // OXO
                // XxX
                // OXO
                // it is required that we use the same shape that was used for what we want to smooth
                val upperVal = values[j + 1 + i * gridSizeX]
                val lowerVal = values[j + 1 + (i + 2) * gridSizeX]
                val leftVal = values[j + (i + 1) * gridSizeX]
                val rightVal = values[j + 2 + (i + 1) * gridSizeX]
                var centerVal = values[j + 1 + (i + 1) * gridSizeX]
                if (upperVal == lowerVal && leftVal == rightVal) {
                    setCoordsSeed(x + j, z + i)
                    centerVal = if (nextInt(2) == 0) upperVal else leftVal
                } else if (upperVal == lowerVal) {
                    centerVal = upperVal
                } else if (leftVal == rightVal) {
                    centerVal = leftVal
                }
                finalValues[j + i * sizeX] = centerVal
            }
        }
        return finalValues
    }
}
