package org.jukeboxmc.world.generator.biomegrid

import org.jukeboxmc.world.generator.noise.bukkit.SimplexOctaveGenerator

class MapLayerNoise(seed: Long) : MapLayer(seed) {
    private val noiseGen: SimplexOctaveGenerator

    init {
        noiseGen = SimplexOctaveGenerator(seed, 2)
    }

    override fun generateValues(x: Int, z: Int, sizeX: Int, sizeZ: Int): IntArray {
        val values = IntArray(sizeX * sizeZ)
        for (i in 0 until sizeZ) {
            for (j in 0 until sizeX) {
                val noise = noiseGen.noise((x + j).toDouble(), (z + i).toDouble(), 0.175, 0.8, true) * 4.0
                var `val`: Int
                `val` = if (noise >= 0.05) {
                    if (noise <= 0.2) 3 else 2
                } else {
                    setCoordsSeed(x + j, z + i)
                    if (nextInt(2) == 0) 3 else 0
                }
                values[j + i * sizeX] = `val`
                //values[j + i * sizeX] = noise >= -0.5D ? (double) noise >= 0.57D ? 2 : noise <= 0.2D ? 3 : 2 : nextInt(2) == 0 ? 3 : 0;
            }
        }
        return values
    }
}