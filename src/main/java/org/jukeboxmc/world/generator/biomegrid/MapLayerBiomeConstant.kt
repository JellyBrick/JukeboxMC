package org.jukeboxmc.world.generator.biomegrid

class MapLayerBiomeConstant(seed: Long, private val biome: Int) : MapLayer(seed) {
    override fun generateValues(x: Int, z: Int, sizeX: Int, sizeZ: Int): IntArray {
        val values = IntArray(sizeX * sizeZ)
        for (i in 0 until sizeZ) {
            for (j in 0 until sizeX) {
                values[j + i * sizeX] = biome
            }
        }
        return values
    }
}
