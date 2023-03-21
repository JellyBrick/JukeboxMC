package org.jukeboxmc.world.generator.biomegrid

import java.util.Random
import org.jukeboxmc.world.Biome
import org.jukeboxmc.world.Dimension

abstract class MapLayer(private val seed: Long) {
    private val random = Random()
    fun setCoordsSeed(x: Int, z: Int) {
        random.setSeed(seed)
        random.setSeed(x * random.nextLong() + z * random.nextLong() xor seed)
    }

    fun nextInt(max: Int): Int {
        return random.nextInt(max)
    }

    abstract fun generateValues(x: Int, z: Int, sizeX: Int, sizeZ: Int): IntArray

    companion object {
        /**
         * Creates the instances for the given map.
         *
         * @param seed      the world seed
         * @param dimension the type of dimension
         * @param worldType the world generator
         * @return an array of all map layers this dimension needs
         */
        fun initialize(seed: Long, dimension: Dimension, worldType: Int): Array<MapLayer?> {
            if (dimension == Dimension.OVERWORLD && worldType == 2) {
                return arrayOf(MapLayerBiomeConstant(seed, Biome.PLAINS.id), null)
            } else if (dimension == Dimension.NETHER) {
                return arrayOf(MapLayerBiomeConstant(seed, Biome.HELL.id), null)
            } else if (dimension == Dimension.THE_END) {
                return arrayOf(MapLayerBiomeConstant(seed, 9), null)
            }
            var zoom = 2
            if (worldType == 5) {
                zoom = 4
            }
            var layer: MapLayer = MapLayerNoise(seed) // this is initial land spread layer
            layer = MapLayerWhittaker(seed + 1, layer, ClimateType.WARM_WET)
            layer = MapLayerWhittaker(seed + 1, layer, ClimateType.COLD_DRY)
            layer = MapLayerWhittaker(seed + 2, layer, ClimateType.LARGER_BIOMES)
            for (i in 0..1) {
                layer = MapLayerZoom(seed + 100 + i, layer, ZoomType.BLURRY)
            }
            for (i in 0..1) {
                layer = MapLayerErosion(seed + 3 + i, layer)
            }
            layer = MapLayerDeepOcean(seed + 4, layer)
            var layerMountains: MapLayer = MapLayerBiomeVariation(seed + 200, layer)
            for (i in 0..1) {
                layerMountains = MapLayerZoom(seed + 200 + i, layerMountains)
            }
            layer = MapLayerBiome(seed + 5, layer)
            for (i in 0..1) {
                layer = MapLayerZoom(seed + 200 + i, layer)
            }
            layer = MapLayerBiomeEdge(seed + 200, layer)
            layer = MapLayerBiomeVariation(seed + 200, layer, layerMountains)
            layer = MapLayerRarePlains(seed + 201, layer)
            layer = MapLayerZoom(seed + 300, layer)
            layer = MapLayerErosion(seed + 6, layer)
            layer = MapLayerZoom(seed + 400, layer)
            layer = MapLayerBiomeEdgeThin(seed + 400, layer)
            layer = MapLayerShore(seed + 7, layer)
            for (i in 0 until zoom) {
                layer = MapLayerZoom(seed + 500 + i, layer)
            }
            var layerRiver = layerMountains
            layerRiver = MapLayerZoom(seed + 300, layerRiver)
            layerRiver = MapLayerZoom(seed + 400, layerRiver)
            for (i in 0 until zoom) {
                layerRiver = MapLayerZoom(seed + 500 + i, layerRiver)
            }
            layerRiver = MapLayerRiver(seed + 10, layerRiver)
            layer = MapLayerRiver(seed + 1000, layerRiver, layer)
            val layerLowerRes = layer
            for (i in 0..1) {
                layer = MapLayerZoom(seed + 2000 + i, layer)
            }
            layer = MapLayerSmooth(seed + 1001, layer)
            return arrayOf(layer, layerLowerRes)
        }
    }
}