package org.jukeboxmc.entity.metadata

import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataMap
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataType
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag

/**
 * @author LucGamesYT
 * @version 1.0
 */
class Metadata {
    private val entityDataMap: EntityDataMap = EntityDataMap()
    fun setByte(entityData: EntityDataType<Byte>?, value: Byte): Metadata {
        val oldValue = getByte(entityData)
        if (oldValue != value) {
            entityDataMap[entityData] = value
        }
        return this
    }

    fun getByte(entityData: EntityDataType<Byte>?): Byte? {
        return entityDataMap[entityData] as? Byte
    }

    fun setLong(entityData: EntityDataType<Long>?, value: Long): Metadata {
        val oldValue = getLong(entityData)
        if (oldValue != value) {
            entityDataMap[entityData] = value
        }
        return this
    }

    fun getLong(entityData: EntityDataType<Long>?): Long? {
        return entityDataMap[entityData] as? Long
    }

    fun setShort(entityData: EntityDataType<Short>?, value: Short): Metadata {
        val oldValue = getShort(entityData)
        if (oldValue != value) {
            entityDataMap[entityData] = value
        }
        return this
    }

    fun getShort(entityData: EntityDataType<Short>?): Short? {
        return entityDataMap[entityData] as? Short
    }

    fun setString(entityData: EntityDataType<String>?, value: String?): Metadata {
        val oldValue = getString(entityData)
        if (oldValue != value) {
            entityDataMap[entityData] = value
        }
        return this
    }

    fun getString(entityData: EntityDataType<String>?): String? {
        return entityDataMap[entityData] as? String
    }

    fun setFloat(entityData: EntityDataType<Float>?, value: Float): Metadata {
        val oldValue = getFloat(entityData)
        if (oldValue != value) {
            entityDataMap[entityData] = value
        }
        return this
    }

    fun getFloat(entityData: EntityDataType<Float>?): Float? {
        return entityDataMap[entityData] as? Float
    }

    fun setFlag(entityFlag: EntityFlag, value: Boolean): Metadata {
        val oldValue = getFlag(entityFlag)
        if (oldValue != value) {
            entityDataMap.setFlag(entityFlag, value)
        }
        return this
    }

    fun getInt(entityData: EntityDataType<Int>?): Int? {
        return entityDataMap[entityData] as? Int
    }

    fun setInt(entityData: EntityDataType<Int>?, value: Int): Metadata {
        val oldValue = getInt(entityData)
        if (oldValue != value) {
            entityDataMap[entityData] = value
        }
        return this
    }

    fun getFlag(entityFlag: EntityFlag): Boolean {
        return entityDataMap.orCreateFlags.contains(entityFlag)
    }

    fun getEntityDataMap(): EntityDataMap {
        return entityDataMap
    }
}
