package org.jukeboxmc.math

/**
 * @author LucGamesYT
 * @version 1.0
 */
class AxisAlignedBB(
    var minX: Float,
    var minY: Float,
    var minZ: Float,
    var maxX: Float,
    var maxY: Float,
    var maxZ: Float,
) : Cloneable {
    fun setBounds(minX: Float, minY: Float, minZ: Float, maxX: Float, maxY: Float, maxZ: Float): AxisAlignedBB {
        this.minX = minX
        this.minY = minY
        this.minZ = minZ
        this.maxX = maxX
        this.maxY = maxY
        this.maxZ = maxZ
        return this
    }

    fun setBounds(other: AxisAlignedBB): AxisAlignedBB {
        minX = other.minX
        minY = other.minY
        minZ = other.minZ
        maxX = other.maxX
        maxY = other.maxY
        maxZ = other.maxZ
        return this
    }

    fun addCoordinates(x: Float, y: Float, z: Float): AxisAlignedBB {
        var minX = minX
        var minY = minY
        var minZ = minZ
        var maxX = maxX
        var maxY = maxY
        var maxZ = maxZ
        if (x < 0) {
            minX += x
        } else if (x > 0) {
            maxX += x
        }
        if (y < 0) {
            minY += y
        } else if (y > 0) {
            maxY += y
        }
        if (z < 0) {
            minZ += z
        } else if (z > 0) {
            maxZ += z
        }
        return AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ)
    }

    fun grow(x: Float, y: Float, z: Float): AxisAlignedBB {
        return AxisAlignedBB(minX - x, minY - y, minZ - z, maxX + x, maxY + y, maxZ + z)
    }

    fun expand(x: Float, y: Float, z: Float): AxisAlignedBB {
        minX -= x
        minY -= y
        minZ -= z
        maxX += x
        maxY += y
        maxZ += z
        return this
    }

    fun offset(x: Float, y: Float, z: Float): AxisAlignedBB {
        minX += x
        minY += y
        minZ += z
        maxX += x
        maxY += y
        maxZ += z
        return this
    }

    fun shrink(x: Float, y: Float, z: Float): AxisAlignedBB {
        return AxisAlignedBB(minX + x, minY + y, minZ + z, maxX - x, maxY - y, maxZ - z)
    }

    fun contract(x: Float, y: Float, z: Float): AxisAlignedBB {
        minX += x
        minY += y
        minZ += z
        maxX -= x
        maxY -= y
        maxZ -= z
        return this
    }

    fun getOffsetBoundingBox(x: Float, y: Float, z: Float): AxisAlignedBB {
        return AxisAlignedBB(minX + x, minY + y, minZ + z, maxX + x, maxY + y, maxZ + z)
    }

    fun calculateXOffset(bb: AxisAlignedBB, x: Float): Float {
        var x = x
        if (bb.maxY <= minY || bb.minY >= maxY) {
            return x
        }
        if (bb.maxZ <= minZ || bb.minZ >= maxZ) {
            return x
        }
        if (x > 0 && bb.maxX <= minX) {
            val x1 = minX - bb.maxX
            if (x1 < x) {
                x = x1
            }
        }
        if (x < 0 && bb.minX >= maxX) {
            val x2 = maxX - bb.minX
            if (x2 > x) {
                x = x2
            }
        }
        return x
    }

    fun calculateYOffset(bb: AxisAlignedBB, y: Float): Float {
        var y = y
        if (bb.maxX <= minX || bb.minX >= maxX) {
            return y
        }
        if (bb.maxZ <= minZ || bb.minZ >= maxZ) {
            return y
        }
        if (y > 0 && bb.maxY <= minY) {
            val y1 = minY - bb.maxY
            if (y1 < y) {
                y = y1
            }
        }
        if (y < 0 && bb.minY >= maxY) {
            val y2 = maxY - bb.minY
            if (y2 > y) {
                y = y2
            }
        }
        return y
    }

    fun calculateZOffset(bb: AxisAlignedBB, z: Float): Float {
        var z = z
        if (bb.maxX <= minX || bb.minX >= maxX) {
            return z
        }
        if (bb.maxY <= minY || bb.minY >= maxY) {
            return z
        }
        if (z > 0 && bb.maxZ <= minZ) {
            val z1 = minZ - bb.maxZ
            if (z1 < z) {
                z = z1
            }
        }
        if (z < 0 && bb.minZ >= maxZ) {
            val z2 = maxZ - bb.minZ
            if (z2 > z) {
                z = z2
            }
        }
        return z
    }

    fun intersectsWith(bb: AxisAlignedBB): Boolean {
        if (bb.maxX - minX > 0.01f && maxX - bb.minX > 0.01f) {
            if (bb.maxY - minY > 0.01f && maxY - bb.minY > 0.01f) {
                return bb.maxZ - minZ > 0.01f && maxZ - bb.minZ > 0.01f
            }
        }
        return false
    }

    fun isVectorInside(vector: Vector): Boolean {
        return !(vector.x <= minX || vector.x >= maxX) &&
            !(vector.y <= minY || vector.y >= maxY) &&
            (vector.z > minZ || vector.z < maxZ)
    }

    val averageEdgeLength: Float
        get() = (maxX - minX + maxY - minY + maxZ - minZ) / 3

    fun isVectorInYZ(vector: Vector): Boolean {
        return vector.y in minY..maxY && vector.z >= minZ && vector.z <= maxZ
    }

    fun isVectorInXZ(vector: Vector): Boolean {
        return vector.x in minX..maxX && vector.z >= minZ && vector.z <= maxZ
    }

    fun isVectorInXY(vector: Vector): Boolean {
        return vector.x in minX..maxX && vector.y >= minY && vector.y <= maxY
    }

    fun calculateIntercept(pos1: Vector, pos2: Vector): Vector? {
        val v1 = pos1.getVectorWhenXIsOnLine(pos2, minX)
        val v2 = pos1.getVectorWhenXIsOnLine(pos2, maxX)
        val v3 = pos1.getVectorWhenYIsOnLine(pos2, minY)
        val v4 = pos1.getVectorWhenYIsOnLine(pos2, maxY)
        val v5 = pos1.getVectorWhenZIsOnLine(pos2, minZ)
        val v6 = pos1.getVectorWhenZIsOnLine(pos2, maxZ)
        var resultVector: Vector? = null
        if (v1 != null && isVectorInYZ(v1)) {
            resultVector = v1
        }
        if (v2 != null && isVectorInYZ(v2) &&
            (resultVector == null || pos1.distanceSquared(v2) < pos1.distanceSquared(resultVector))
        ) {
            resultVector = v2
        }
        if (v3 != null && isVectorInXZ(v3) &&
            (resultVector == null || pos1.distanceSquared(v3) < pos1.distanceSquared(resultVector))
        ) {
            resultVector = v3
        }
        if (v4 != null && isVectorInXZ(v4) &&
            (resultVector == null || pos1.distanceSquared(v4) < pos1.distanceSquared(resultVector))
        ) {
            resultVector = v4
        }
        if (v5 != null && isVectorInXY(v5) &&
            (resultVector == null || pos1.distanceSquared(v5) < pos1.distanceSquared(resultVector))
        ) {
            resultVector = v5
        }
        if (v6 != null && isVectorInXY(v6) &&
            (resultVector == null || pos1.distanceSquared(v6) < pos1.distanceSquared(resultVector))
        ) {
            resultVector = v6
        }
        return resultVector
    }

    public override fun clone(): AxisAlignedBB {
        return try {
            val clone = super.clone() as AxisAlignedBB
            clone.setBounds(this)
        } catch (e: CloneNotSupportedException) {
            AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ)
        }
    }
}
