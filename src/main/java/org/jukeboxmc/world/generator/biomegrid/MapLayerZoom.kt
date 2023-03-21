package org.jukeboxmc.world.generator.biomegrid

class MapLayerZoom
/**
 * Creates a map layer.
 *
 * @param seed the layer random seed
 * @param belowLayer the layer generated before this one
 * @param zoomType the zoom-type parameter
 */ @JvmOverloads constructor(
    seed: Long,
    private val belowLayer: MapLayer,
    private val zoomType: ZoomType = ZoomType.NORMAL,
) : MapLayer(seed) {
    override fun generateValues(x: Int, z: Int, sizeX: Int, sizeZ: Int): IntArray {
        val gridX = x shr 1
        val gridZ = z shr 1
        val gridSizeX = (sizeX shr 1) + 2
        val gridSizeZ = (sizeZ shr 1) + 2
        val values = belowLayer.generateValues(gridX, gridZ, gridSizeX, gridSizeZ)
        val zoomSizeX = gridSizeX - 1 shl 1
        val zoomSizeZ = gridSizeZ - 1 shl 1
        val tmpValues = IntArray(zoomSizeX * zoomSizeZ)
        for (i in 0 until gridSizeZ - 1) {
            var n = i * 2 * zoomSizeX
            var upperLeftVal = values!![i * gridSizeX]
            var lowerLeftVal = values[(i + 1) * gridSizeX]
            for (j in 0 until gridSizeX - 1) {
                setCoordsSeed(gridX + j shl 1, gridZ + i shl 1)
                tmpValues[n] = upperLeftVal
                tmpValues[n + zoomSizeX] = if (nextInt(2) > 0) upperLeftVal else lowerLeftVal
                val upperRightVal = values[j + 1 + i * gridSizeX]
                val lowerRightVal = values[j + 1 + (i + 1) * gridSizeX]
                tmpValues[n + 1] = if (nextInt(2) > 0) upperLeftVal else upperRightVal
                tmpValues[n + 1 + zoomSizeX] = getNearest(upperLeftVal, upperRightVal, lowerLeftVal, lowerRightVal)
                upperLeftVal = upperRightVal
                lowerLeftVal = lowerRightVal
                n += 2
            }
        }
        val finalValues = IntArray(sizeX * sizeZ)
        for (i in 0 until sizeZ) {
            for (j in 0 until sizeX) {
                finalValues[j + i * sizeX] = tmpValues[j + (i + (z and 1)) * zoomSizeX + (x and 1)]
            }
        }
        return finalValues
    }

    private fun getNearest(upperLeftVal: Int, upperRightVal: Int, lowerLeftVal: Int, lowerRightVal: Int): Int {
        if (zoomType == ZoomType.NORMAL) {
            if (upperRightVal == lowerLeftVal && lowerLeftVal == lowerRightVal) {
                return upperRightVal
            } else if (upperLeftVal == upperRightVal && upperLeftVal == lowerLeftVal) {
                return upperLeftVal
            } else if (upperLeftVal == upperRightVal && upperLeftVal == lowerRightVal) {
                return upperLeftVal
            } else if (upperLeftVal == lowerLeftVal && upperLeftVal == lowerRightVal) {
                return upperLeftVal
            } else if (upperLeftVal == upperRightVal && lowerLeftVal != lowerRightVal) {
                return upperLeftVal
            } else if (upperLeftVal == lowerLeftVal && upperRightVal != lowerRightVal) {
                return upperLeftVal
            } else if (upperLeftVal == lowerRightVal && upperRightVal != lowerLeftVal) {
                return upperLeftVal
            } else if (upperRightVal == lowerLeftVal && upperLeftVal != lowerRightVal) {
                return upperRightVal
            } else if (upperRightVal == lowerRightVal && upperLeftVal != lowerLeftVal) {
                return upperRightVal
            } else if (lowerLeftVal == lowerRightVal && upperLeftVal != upperRightVal) {
                return lowerLeftVal
            }
        }
        val values = intArrayOf(upperLeftVal, upperRightVal, lowerLeftVal, lowerRightVal)
        return values[nextInt(values.size)]
    }

    enum class ZoomType {
        NORMAL, BLURRY
    }
}
