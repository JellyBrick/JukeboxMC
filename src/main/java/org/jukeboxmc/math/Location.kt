package org.jukeboxmc.math

import org.jukeboxmc.block.Block
import org.jukeboxmc.world.Biome
import org.jukeboxmc.world.Dimension
import org.jukeboxmc.world.World
import org.jukeboxmc.world.chunk.Chunk
import kotlin.math.cos
import kotlin.math.sin

/**
 * @author LucGamesYT
 * @version 1.0
 */
class Location @JvmOverloads constructor(
    var world: World?,
    x: Float,
    y: Float,
    z: Float,
    var yaw: Float = 0f,
    var pitch: Float = 0f,
    dimension: Dimension = Dimension.OVERWORLD
) : Vector(x, y, z), Cloneable {

    init {
        this.dimension = dimension
    }

    constructor(world: World?, x: Float, y: Float, z: Float, dimension: Dimension?) : this(
        world,
        x,
        y,
        z,
        0f,
        0f,
        dimension
    )

    constructor(world: World?, x: Int, y: Int, z: Int, dimension: Dimension?) : this(
        world,
        x.toFloat(),
        y.toFloat(),
        z.toFloat(),
        0f,
        0f,
        dimension
    )

    constructor(world: World?, x: Int, y: Int, z: Int) : this(
        world,
        x.toFloat(),
        y.toFloat(),
        z.toFloat(),
        0f,
        0f,
        Dimension.OVERWORLD
    )

    constructor(world: World?, vector: Vector, yaw: Float, pitch: Float, dimension: Dimension?) : this(
        world,
        vector.x,
        vector.y,
        vector.z,
        yaw,
        pitch,
        dimension
    )

    constructor(world: World?, vector: Vector, yaw: Float, pitch: Float) : this(
        world,
        vector.x,
        vector.y,
        vector.z,
        yaw,
        pitch,
        Dimension.OVERWORLD
    )

    constructor(world: World?, vector: Vector, dimension: Dimension?) : this(
        world,
        vector.x,
        vector.y,
        vector.z,
        0f,
        0f,
        dimension
    )

    constructor(world: World?, vector: Vector) : this(
        world,
        vector.x,
        vector.y,
        vector.z,
        0f,
        0f,
        Dimension.OVERWORLD
    )

    override fun add(x: Float, y: Float, z: Float): Location {
        return Location(world, this.x + x, this.y + y, this.z + z, yaw, pitch, dimension)
    }

    override fun subtract(x: Float, y: Float, z: Float): Location {
        return Location(world, this.x - x, this.y - y, this.z - z, yaw, pitch, dimension)
    }

    override fun multiply(x: Float, y: Float, z: Float): Location {
        return Location(world, this.x * x, this.y * y, this.z * z, yaw, pitch, dimension)
    }

    override fun divide(x: Float, y: Float, z: Float): Location {
        return Location(world, this.x / x, this.y / y, this.z / z, yaw, pitch, dimension)
    }

    val block: Block?
        get() = world!!.getBlock(this)
    val biome: Biome?
        get() = world?.getBiome(this, dimension)

    fun getBlock(layer: Int): Block? {
        return world?.getBlock(this, layer)
    }

    val chunk: Chunk?
        get() = world?.getChunk(this.blockX shr 4, this.blockZ shr 4, dimension)
    val loadedChunk: Chunk?
        get() = world?.getLoadedChunk(this.blockX shr 4, this.blockZ shr 4, dimension)
    val direction: Vector
        get() {
            val pitch = (pitch + 90) * Math.PI / 180
            val yaw = (yaw + 90) * Math.PI / 180
            val x = sin(pitch) * cos(yaw)
            val z = sin(pitch) * sin(yaw)
            val y = cos(pitch)
            return Vector(x.toFloat(), y.toFloat(), z.toFloat()).normalize()
        }

    override fun clone(): Location {
        val clone: Vector = super<Vector>.clone()
        return Location(world, clone)
    }

    override fun toString(): String {
        return "Location{" +
                "world=" + world.name +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", yaw=" + yaw +
                ", pitch=" + pitch +
                ", dimension=" + dimension.dimensionName +
                '}'
    }
}