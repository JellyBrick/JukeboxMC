package org.jukeboxmc.world.generator

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockStone
import org.jukeboxmc.block.data.StoneType
import org.jukeboxmc.math.Vector
import org.jukeboxmc.world.Biome
import org.jukeboxmc.world.Dimension
import org.jukeboxmc.world.World
import org.jukeboxmc.world.chunk.Chunk
import org.jukeboxmc.world.chunk.manager.PopulationChunkManager
import org.jukeboxmc.world.generator.biome.BiomeGrid
import org.jukeboxmc.world.generator.biome.BiomeHeight
import org.jukeboxmc.world.generator.biome.GroundGenerator
import org.jukeboxmc.world.generator.biome.generation.GroundGeneratorMycel
import org.jukeboxmc.world.generator.biome.generation.GroundGeneratorPatchDirt
import org.jukeboxmc.world.generator.biome.generation.GroundGeneratorPatchDirtAndStone
import org.jukeboxmc.world.generator.biome.generation.GroundGeneratorPatchGravel
import org.jukeboxmc.world.generator.biome.generation.GroundGeneratorPatchStone
import org.jukeboxmc.world.generator.biome.generation.GroundGeneratorRocky
import org.jukeboxmc.world.generator.biome.generation.GroundGeneratorSandy
import org.jukeboxmc.world.generator.biome.generation.GroundGeneratorSnowy
import org.jukeboxmc.world.generator.biomegrid.MapLayer
import org.jukeboxmc.world.generator.noise.PerlinOctaveGenerator
import org.jukeboxmc.world.generator.noise.SimplexOctaveGenerator
import org.jukeboxmc.world.generator.populator.OrePopulator
import org.jukeboxmc.world.generator.populator.Populator
import org.jukeboxmc.world.generator.populator.biome.BiomePopulatorRegistry
import org.jukeboxmc.world.generator.thing.OreType
import java.util.EnumMap
import java.util.Random
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * @author LucGamesYT
 * @version 1.0
 */
open class NormalGenerator(private val world: World) : Generator() {
    private val biomeGrid: Array<MapLayer?>
    private val random: Random = Random()
    private val blockStone: Block
    private val blockWater: Block
    private val blockBedrock: Block
    private val localSeed1: Long
    private val localSeed2: Long
    private val heightGenerator: PerlinOctaveGenerator
    private val roughnessGenerator: PerlinOctaveGenerator
    private val roughness2Generator: PerlinOctaveGenerator
    private val detailGenerator: PerlinOctaveGenerator
    private val surfaceGenerator: SimplexOctaveGenerator
    private val populators: MutableSet<Populator> = HashSet()
    private val density = Array(5) { Array(5) { DoubleArray(33) } }
    private val groundGenerator = GroundGenerator()

    init {
        random.setSeed(world.seed)
        biomeGrid = MapLayer.initialize(world.seed, Dimension.OVERWORLD, 1)
        localSeed1 = ThreadLocalRandom.current().nextLong()
        localSeed2 = ThreadLocalRandom.current().nextLong()
        heightGenerator = PerlinOctaveGenerator(random, 16, 5, 5)
        heightGenerator.xScale = 200.0
        heightGenerator.zScale = 200.0
        roughnessGenerator = PerlinOctaveGenerator(random, 16, 5, 33, 5)
        roughnessGenerator.xScale = 684.412
        roughnessGenerator.yScale = 684.412
        roughnessGenerator.zScale = 684.412
        roughness2Generator = PerlinOctaveGenerator(random, 16, 5, 33, 5)
        roughness2Generator.xScale = 684.412
        roughness2Generator.yScale = 684.412
        roughness2Generator.zScale = 684.412
        detailGenerator = PerlinOctaveGenerator(random, 8, 5, 33, 5)
        detailGenerator.xScale = 684.412 / 80.0
        detailGenerator.yScale = 684.412 / 160.0
        detailGenerator.zScale = 684.412 / 80.0
        surfaceGenerator = SimplexOctaveGenerator(random, 4, 16, 16)
        surfaceGenerator.setScale(0.0625)
        blockStone = Block.create(BlockType.STONE)
        blockWater = Block.create(BlockType.WATER)
        blockBedrock = Block.create(BlockType.BEDROCK)
        populators.add(
            OrePopulator(
                arrayOf(
                    OreType(Block.create(BlockType.COAL_ORE), 20, 17, 0, 128),
                    OreType(Block.create(BlockType.IRON_ORE), 20, 9, 0, 64),
                    OreType(Block.create(BlockType.REDSTONE_ORE), 8, 8, 0, 16),
                    OreType(Block.create(BlockType.LAPIS_ORE), 1, 7, 0, 16),
                    OreType(Block.create(BlockType.GOLD_ORE), 2, 9, 0, 32),
                    OreType(Block.create(BlockType.DIAMOND_ORE), 1, 8, 0, 16),
                    OreType(Block.create(BlockType.DIRT), 10, 33, 0, 128),
                    OreType(Block.create(BlockType.GRAVEL), 8, 33, 0, 128),
                    OreType(
                        Block.create<BlockStone>(BlockType.STONE).setStoneType(StoneType.GRANITE),
                        10,
                        33,
                        0,
                        80,
                    ),
                    OreType(
                        Block.create<BlockStone>(BlockType.STONE).setStoneType(StoneType.ANDESITE),
                        10,
                        33,
                        0,
                        80,
                    ),
                    OreType(
                        Block.create<BlockStone>(BlockType.STONE).setStoneType(StoneType.DIORITE),
                        10,
                        33,
                        0,
                        80,
                    ),
                ),
            ),
        )
    }

    override fun generate(chunk: Chunk, chunkX: Int, chunkZ: Int) {
        random.setSeed(chunkX * localSeed1 xor chunkZ * localSeed2 xor world.seed)
        val x = chunkX shl 2
        val z = chunkZ shl 2
        val biomeGrid: IntArray = biomeGrid[1]?.generateValues(x - 2, z - 2, 10, 10) ?: return // FIXME
        val heightNoise = heightGenerator.getFractalBrownianMotion(x.toDouble(), z.toDouble(), 0.5, 2.0)
        val roughnessNoise = roughnessGenerator.getFractalBrownianMotion(x.toDouble(), 0.0, z.toDouble(), 0.5, 2.0)
        val roughnessNoise2 = roughness2Generator.getFractalBrownianMotion(x.toDouble(), 0.0, z.toDouble(), 0.5, 2.0)
        val detailNoise = detailGenerator.getFractalBrownianMotion(x.toDouble(), 0.0, z.toDouble(), 0.5, 2.0)
        var index = 0
        var indexHeight = 0
        for (xSeg in 0..4) {
            for (zSeg in 0..4) {
                var avgHeightScale = 0.0
                var avgHeightBase = 0.0
                var totalWeight = 0.0
                val biome: Biome = Biome.findById(biomeGrid[xSeg + 2 + (zSeg + 2) * 10])
                val biomeHeight = HEIGHT_MAP.getOrDefault(biome, BiomeHeight.DEFAULT)
                for (xSmooth in 0..4) {
                    for (zSmooth in 0..4) {
                        val nearBiome: Biome =
                            Biome.findById(biomeGrid[xSeg + xSmooth + (zSeg + zSmooth) * 10])
                        val nearBiomeHeight = HEIGHT_MAP.getOrDefault(nearBiome, BiomeHeight.DEFAULT)
                        val heightBase = nearBiomeHeight.height
                        val heightScale = nearBiomeHeight.scale
                        var weight = ELEVATION_WEIGHT[xSmooth][zSmooth] / (heightBase + 2.0)
                        if (nearBiomeHeight.height > biomeHeight.height) {
                            weight *= 0.5
                        }
                        avgHeightScale += heightScale * weight
                        avgHeightBase += heightBase * weight
                        totalWeight += weight
                    }
                }
                avgHeightScale /= totalWeight
                avgHeightBase /= totalWeight
                avgHeightScale = avgHeightScale * 0.9 + 0.1
                avgHeightBase = (avgHeightBase * 4.0 - 1.0) / 8.0
                var noiseH = heightNoise!![indexHeight++] / 8000.0
                if (noiseH < 0) {
                    noiseH = abs(noiseH) * 0.3
                }
                noiseH = noiseH * 3.0 - 2.0
                noiseH = if (noiseH < 0) {
                    max(noiseH * 0.5, -1.0) / 1.4 * 0.5
                } else {
                    min(noiseH, 1.0) / 8.0
                }
                noiseH = (noiseH * 0.2 + avgHeightBase) * 8.5 / 8.0 * 4.0 + 8.5
                for (k in 0..32) {
                    var nh = (k - noiseH) * 12.0 * 128.0 / 256.0 / avgHeightScale
                    if (nh < 0) {
                        nh *= 4.0
                    }
                    val noiseR = roughnessNoise!![index] / 512.0
                    val noiseR2 = roughnessNoise2!![index] / 512.0
                    val noiseD = (detailNoise!![index] / 10.0 + 1.0) / 2.0
                    var dens =
                        if (noiseD < 0) noiseR else if (noiseD > 1) noiseR2 else noiseR + (noiseR2 - noiseR) * noiseD
                    dens -= nh
                    index++
                    if (k > 29) {
                        val lowering = (k - 29) / 3.0
                        dens = dens * (1.0 - lowering) + -10.0 * lowering
                    }
                    density[xSeg][zSeg][k] = dens
                }
            }
        }
        val densityOffset = 0.0
        for (i in 0 until 5 - 1) {
            for (j in 0 until 5 - 1) {
                for (k in 0 until 33 - 1) {
                    var d1 = density[i][j][k]
                    var d2 = density[i + 1][j][k]
                    var d3 = density[i][j + 1][k]
                    var d4 = density[i + 1][j + 1][k]
                    val d5 = (density[i][j][k + 1] - d1) / 8
                    val d6 = (density[i + 1][j][k + 1] - d2) / 8
                    val d7 = (density[i][j + 1][k + 1] - d3) / 8
                    val d8 = (density[i + 1][j + 1][k + 1] - d4) / 8
                    for (l in 0..7) {
                        var d9 = d1
                        var d10 = d3
                        for (m in 0..3) {
                            var dens = d9
                            for (n in 0..3) {
                                if (dens > densityOffset) {
                                    chunk.setBlock(m + (i shl 2), l + (k shl 3), n + (j shl 2), 0, blockStone)
                                } else if (l + (k shl 3) < WATER_HEIGHT - 1) {
                                    chunk.setBlock(m + (i shl 2), l + (k shl 3), n + (j shl 2), 0, blockWater)
                                }
                                dens += (d10 - d9) / 4
                            }
                            d9 += (d2 - d1) / 4
                            d10 += (d4 - d3) / 4
                        }
                        d1 += d5
                        d3 += d7
                        d2 += d6
                        d4 += d8
                    }
                }
            }
        }
        val cx = chunkX shl 4
        val cz = chunkZ shl 4
        val biomes = BiomeGrid()
        val biomeValues: IntArray = this.biomeGrid[0]?.generateValues(cx, cz, 16, 16) ?: return // FIXME
        for (i in biomeValues.indices) {
            biomes.biomes[i] = biomeValues[i].toByte()
        }
        val octaveGenerator = surfaceGenerator
        val sizeX = octaveGenerator.sizeX
        val sizeZ = octaveGenerator.sizeZ
        val surfaceNoise = octaveGenerator.getFractalBrownianMotion(cx.toDouble(), cz.toDouble(), 0.5, 0.5)
        for (sx in 0 until sizeX) {
            for (sz in 0 until sizeZ) {
                GROUND_MAP.getOrDefault(biomes.getBiome(sx, sz), groundGenerator)
                    .generateTerrainColumn(chunk, random, cx + sx, cz + sz, surfaceNoise!![sx or (sz shl 4)])
                for (y in chunk.minY until chunk.maxY) {
                    chunk.setBiome(sx, y, sz, biomes.getBiome(sx, sz))
                }
                chunk.setBlock(sx, 0, sz, 0, blockBedrock)
            }
        }
    }

    override fun populate(manager: PopulationChunkManager, chunkX: Int, chunkZ: Int) {
        random.setSeed(0XDEADBEEFL xor (chunkX.toLong() shl 8) xor chunkZ.toLong() xor world.seed)
        val chunk = manager.getChunk(chunkX, chunkZ) ?: return
        populators.forEach {
            it.populate(random, chunk.world, manager, chunkX, chunkZ)
        }
        val biome = chunk.getBiome(7, 7, 7) ?: return
        val biomePopulator = BiomePopulatorRegistry.getBiomePopulator(biome) ?: return
        biomePopulator.getPopulators().forEach {
            it.populate(random, chunk.world, manager, chunkX, chunkZ)
        }
    }

    override fun finish(manager: PopulationChunkManager, chunkX: Int, chunkZ: Int) {}
    override val spawnLocation: Vector
        get() = Vector(0, 100, 0)

    companion object {
        const val WATER_HEIGHT = 64
        private val HEIGHT_MAP: MutableMap<Biome?, BiomeHeight> = EnumMap(org.jukeboxmc.world.Biome::class.java)
        private val GROUND_MAP: MutableMap<Biome, GroundGenerator> = EnumMap(org.jukeboxmc.world.Biome::class.java)
        private val ELEVATION_WEIGHT = Array(5) { DoubleArray(5) }

        init {
            setBiomeGround(
                GroundGeneratorSandy(),
                Biome.BEACH,
                Biome.COLD_BEACH,
                Biome.DESERT,
                Biome.DESERT_HILLS,
                Biome.DESERT_MUTATED,
            )
            setBiomeGround(GroundGeneratorRocky(), Biome.STONE_BEACH)
            setBiomeGround(GroundGeneratorSnowy(), Biome.ICE_PLAINS_SPIKES)
            setBiomeGround(GroundGeneratorMycel(), Biome.MUSHROOM_ISLAND, Biome.MUSHROOM_ISLAND_SHORE)
            setBiomeGround(GroundGeneratorPatchStone(), Biome.EXTREME_HILLS)
            setBiomeGround(
                GroundGeneratorPatchGravel(),
                Biome.EXTREME_HILLS_MUTATED,
                Biome.EXTREME_HILLS_PLUS_TREES_MUTATED,
            )
            setBiomeGround(GroundGeneratorPatchDirtAndStone(), Biome.SAVANNA_MUTATED, Biome.SAVANNA_PLATEAU_MUTATED)
            setBiomeGround(
                GroundGeneratorPatchDirt(),
                Biome.MEGA_TAIGA,
                Biome.MEGA_TAIGA_HILLS,
                Biome.REDWOOD_TAIGA_MUTATED,
            )
            // setBiomeGround( new GroundGeneratorMesa(), Biome.MESA, Biome.MESA_PLATEAU, Biome.MESA_PLATEAU_STONE );
            // setBiomeGround( new GroundGeneratorMesa( GroundGeneratorMesa.MesaType.BRYCE ), Biome.MESA_BRYCE );
            // setBiomeGround( new GroundGeneratorMesa( GroundGeneratorMesa.MesaType.FOREST ), Biome.MESA_PLATEAU_STONE, Biome.MESA_PLATEAU_STONE_MUTATED );
            setBiomeHeight(BiomeHeight.OCEAN, Biome.OCEAN, Biome.FROZEN_OCEAN)
            setBiomeHeight(BiomeHeight.DEEP_OCEAN, Biome.DEEP_OCEAN)
            setBiomeHeight(BiomeHeight.RIVER, Biome.RIVER, Biome.FROZEN_RIVER)
            setBiomeHeight(BiomeHeight.FLAT_SHORE, Biome.BEACH, Biome.COLD_BEACH, Biome.MUSHROOM_ISLAND_SHORE)
            setBiomeHeight(BiomeHeight.ROCKY_SHORE, Biome.STONE_BEACH)
            setBiomeHeight(BiomeHeight.FLATLANDS, Biome.DESERT, Biome.ICE_PLAINS, Biome.SAVANNA, Biome.PLAINS)
            setBiomeHeight(
                BiomeHeight.EXTREME_HILLS,
                Biome.EXTREME_HILLS,
                Biome.EXTREME_HILLS_PLUS_TREES,
                Biome.EXTREME_HILLS_MUTATED,
                Biome.EXTREME_HILLS_PLUS_TREES_MUTATED,
            )
            setBiomeHeight(BiomeHeight.MID_PLAINS, Biome.TAIGA, Biome.COLD_TAIGA, Biome.MEGA_TAIGA)
            setBiomeHeight(BiomeHeight.SWAMPLAND, Biome.SWAMPLAND)
            setBiomeHeight(BiomeHeight.LOW_HILLS, Biome.MUSHROOM_ISLAND)
            setBiomeHeight(
                BiomeHeight.HILLS,
                Biome.DESERT_HILLS,
                Biome.FOREST_HILLS,
                Biome.TAIGA_HILLS,
                Biome.EXTREME_HILLS_EDGE,
                Biome.JUNGLE_HILLS,
                Biome.BIRCH_FOREST_HILLS,
                Biome.COLD_TAIGA_HILLS,
                Biome.MEGA_TAIGA_HILLS,
                Biome.MESA_PLATEAU_STONE_MUTATED,
                Biome.MESA_PLATEAU_MUTATED,
            ) // , Biome.ICE_MOUNTAINS
            setBiomeHeight(
                BiomeHeight.HIGH_PLATEAU,
                Biome.SAVANNA_PLATEAU,
                Biome.MESA_PLATEAU_STONE,
                Biome.MESA_PLATEAU,
            )
            setBiomeHeight(BiomeHeight.FLATLANDS_HILLS, Biome.DESERT_MUTATED)
            setBiomeHeight(BiomeHeight.BIG_HILLS, Biome.ICE_PLAINS_SPIKES)
            setBiomeHeight(BiomeHeight.BIG_HILLS2, Biome.BIRCH_FOREST_HILLS_MUTATED)
            setBiomeHeight(BiomeHeight.SWAMPLAND_HILLS, Biome.SWAMPLAND_MUTATED)
            setBiomeHeight(
                BiomeHeight.DEFAULT_HILLS,
                Biome.JUNGLE_MUTATED,
                Biome.JUNGLE_EDGE_MUTATED,
                Biome.BIRCH_FOREST_MUTATED,
                Biome.ROOFED_FOREST_MUTATED,
            )
            setBiomeHeight(
                BiomeHeight.MID_HILLS,
                Biome.TAIGA_MUTATED,
                Biome.COLD_TAIGA_MUTATED,
                Biome.REDWOOD_TAIGA_MUTATED,
            )
            setBiomeHeight(BiomeHeight.MID_HILLS2, Biome.FLOWER_FOREST)
            setBiomeHeight(BiomeHeight.LOW_SPIKES, Biome.SAVANNA_MUTATED)
            setBiomeHeight(BiomeHeight.HIGH_SPIKES, Biome.SAVANNA_PLATEAU_MUTATED)
            for (x in 0..4) {
                for (z in 0..4) {
                    var sqX = x - 2
                    sqX *= sqX
                    var sqZ = z - 2
                    sqZ *= sqZ
                    ELEVATION_WEIGHT[x][z] = 10.0 / sqrt(sqX + sqZ + 0.2)
                }
            }
        }

        private fun setBiomeHeight(height: BiomeHeight, vararg biomes: Biome) {
            for (biome in biomes) {
                HEIGHT_MAP[biome] = height
            }
        }

        protected fun setBiomeGround(groundGenerator: GroundGenerator, vararg biomes: Biome) {
            for (biome in biomes) {
                GROUND_MAP[biome] = groundGenerator
            }
        }
    }
}
