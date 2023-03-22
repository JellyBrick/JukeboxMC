package org.jukeboxmc.world.generator.biomegrid

import it.unimi.dsi.fastutil.ints.Int2IntMap
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import org.jukeboxmc.world.Biome

class MapLayerRarePlains(seed: Long, private val belowLayer: MapLayer) : MapLayer(seed) {
    override fun generateValues(x: Int, z: Int, sizeX: Int, sizeZ: Int): IntArray {
        val gridX = x - 1
        val gridZ = z - 1
        val gridSizeX = sizeX + 2
        val gridSizeZ = sizeZ + 2
        val values = belowLayer.generateValues(gridX, gridZ, gridSizeX, gridSizeZ)
        val finalValues = IntArray(sizeX * sizeZ)
        for (i in 0 until sizeZ) {
            for (j in 0 until sizeX) {
                setCoordsSeed(x + j, z + i)
                var centerValue = values[j + 1 + (i + 1) * gridSizeX]
                if (nextInt(57) == 0 && RARE_PLAINS.containsKey(centerValue)) {
                    centerValue = RARE_PLAINS[centerValue]
                }
                finalValues[j + i * sizeX] = centerValue
            }
        }
        return finalValues
    }

    companion object {
        private val RARE_PLAINS: Int2IntMap = Int2IntOpenHashMap()

        init {
            RARE_PLAINS.put(Biome.PLAINS.id, Biome.SUNFLOWER_PLAINS.id)
        }
    }
}
