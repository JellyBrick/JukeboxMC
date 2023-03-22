package org.jukeboxmc.math

import com.nukkitx.math.vector.Vector3d
import com.nukkitx.math.vector.Vector3f
import com.nukkitx.math.vector.Vector3i
import com.nukkitx.math.vector.Vector3l
import org.apache.commons.math3.util.FastMath
import org.jukeboxmc.world.Dimension
import java.util.*

/**
 * @author LucGamesYT
 * @version 1.0
 */
open class Vector : Cloneable {
    var x: Float
    var y: Float
    var z: Float
    var dimension: Dimension = Dimension.OVERWORLD

    constructor(x: Float, y: Float, z: Float, dimension: Dimension?) {
        this.x = x
        this.y = y
        this.z = z
        this.dimension = dimension ?: Dimension.OVERWORLD
    }

    constructor(x: Int, y: Int, z: Int, dimension: Dimension?) {
        this.x = x.toFloat()
        this.y = y.toFloat()
        this.z = z.toFloat()
        this.dimension = dimension ?: Dimension.OVERWORLD
    }

    constructor(x: Float, y: Float, z: Float) {
        this.x = x
        this.y = y
        this.z = z
    }

    constructor(x: Int, y: Int, z: Int) {
        this.x = x.toFloat()
        this.y = y.toFloat()
        this.z = z.toFloat()
    }

    constructor(vector3f: Vector3f) {
        x = vector3f.x
        y = vector3f.y
        z = vector3f.z
    }

    constructor(vector3i: Vector3i) {
        x = vector3i.x.toFloat()
        y = vector3i.y.toFloat()
        z = vector3i.z.toFloat()
    }

    fun up(): Vector {
        return Vector(x, y + 1, z)
    }

    fun down(): Vector {
        return Vector(x, y - 1, z)
    }

    fun north(): Vector {
        return Vector(x, y, z - 1)
    }

    fun east(): Vector {
        return Vector(x + 1, y, z)
    }

    fun south(): Vector {
        return Vector(x, y, z + 1)
    }

    fun west(): Vector {
        return Vector(x - 1, y, z)
    }

    fun setVector(x: Int, y: Int, z: Int) {
        this.x = x.toFloat()
        this.y = y.toFloat()
        this.z = z.toFloat()
    }

    val blockX: Int
        get() = FastMath.floor(x.toDouble()).toInt()
    val blockY: Int
        get() = FastMath.floor(y.toDouble()).toInt()
    val blockZ: Int
        get() = FastMath.floor(z.toDouble()).toInt()
    val chunkX: Int
        get() = blockX shr 4
    val chunkZ: Int
        get() = blockZ shr 4

    fun getVectorWhenXIsOnLine(other: Vector, x: Float): Vector? {
        val xDiff = other.x - this.x
        val yDiff = other.y - y
        val zDiff = other.z - z
        val f = (x - this.x) / xDiff
        return if (f >= 0f && f <= 1f) Vector(this.x + xDiff * f, y + yDiff * f, z + zDiff * f, dimension) else null
    }

    fun getVectorWhenYIsOnLine(other: Vector, y: Float): Vector? {
        val xDiff = other.x - x
        val yDiff = other.y - this.y
        val zDiff = other.z - z
        val f = (y - this.y) / yDiff
        return if (f >= 0f && f <= 1f) Vector(x + xDiff * f, this.y + yDiff * f, z + zDiff * f, dimension) else null
    }

    fun getVectorWhenZIsOnLine(other: Vector, z: Float): Vector? {
        val xDiff = other.x - x
        val yDiff = other.y - y
        val zDiff = other.z - this.z
        val f = (z - this.z) / zDiff
        return if (f >= 0f && f <= 1f) Vector(x + xDiff * f, y + yDiff * f, this.z + zDiff * f, dimension) else null
    }

    open fun add(x: Float, y: Float, z: Float): Vector {
        return Vector(this.x + x, this.y + y, this.z + z, dimension)
    }

    open fun subtract(x: Float, y: Float, z: Float): Vector {
        return Vector(this.x - x, this.y - y, this.z - z, dimension)
    }

    open fun multiply(x: Float, y: Float, z: Float): Vector {
        return Vector(this.x * x, this.y * y, this.z * z, dimension)
    }

    open fun divide(x: Float, y: Float, z: Float): Vector {
        return Vector(this.x / x, this.y / y, this.z / z, dimension)
    }

    fun normalize(): Vector {
        val squaredLength = squaredLength()
        return divide(squaredLength, squaredLength, squaredLength)
    }

    fun distance(vector: Vector): Float {
        return Math.sqrt(distanceSquared(vector).toDouble()).toFloat()
    }

    fun distanceSquared(vector: Vector): Float {
        return (
            FastMath.pow((x - vector.x).toDouble(), 2) + FastMath.pow((y - vector.y).toDouble(), 2) +
                FastMath.pow((z - vector.z).toDouble(), 2)
            ).toFloat()
    }

    fun squaredLength(): Float {
        return FastMath.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
    }

    fun toVector3f(): Vector3f {
        return Vector3f.from(x, y, z)
    }

    fun toVector3D(): Vector3d {
        return Vector3d.from(x, y, z)
    }

    fun toVector3l(): Vector3l {
        return Vector3l.from(x.toDouble(), y.toDouble(), z.toDouble())
    }

    fun toVector3i(): Vector3i {
        return Vector3i.from(x.toDouble(), y.toDouble(), z.toDouble())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val vector = other as Vector
        return vector.x.compareTo(x) == 0 && vector.y.compareTo(y) == 0 && vector.z.compareTo(z) == 0 && dimension == vector.dimension
    }

    override fun hashCode(): Int {
        return Objects.hash(x, y, z, dimension)
    }

    public override fun clone(): Vector {
        val clone = super.clone() as Vector
        clone.x = x
        clone.y = y
        clone.z = z
        return clone
    }

    override fun toString(): String {
        return "Vector{" +
            "x=" + x +
            ", y=" + y +
            ", z=" + z +
            ", dimension=" + dimension +
            '}'
    }

    fun round(): Vector {
        return Vector(Math.round(x), Math.round(y), Math.round(z))
    }

    companion object {
        fun zero(): Vector {
            return Vector(0, 0, 0)
        }
    }
}
