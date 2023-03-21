package org.jukeboxmc.entity.metadata

import com.nukkitx.protocol.bedrock.data.entity.EntityData

/**
 * @author LucGamesYT
 * @version 1.0
 */
class Metadata {
    private val entityDataMap: EntityDataMap = EntityDataMap()
    fun setByte(entityData: EntityData?, value: Byte): Metadata {
        val oldValue = getByte(entityData)
        if (oldValue != value) {
            entityDataMap.putByte(entityData, value.toInt())
        }
        return this
    }

    fun getByte(entityData: EntityData?): Byte {
        return entityDataMap.getByte(entityData)
    }

    fun setLong(entityData: EntityData?, value: Long): Metadata {
        val oldValue = getLong(entityData)
        if (oldValue != value) {
            entityDataMap.putLong(entityData, value)
        }
        return this
    }

    fun getLong(entityData: EntityData?): Long {
        return entityDataMap.getLong(entityData)
    }

    fun setShort(entityData: EntityData?, value: Short): Metadata {
        val oldValue = getShort(entityData)
        if (oldValue != value) {
            entityDataMap.putShort(entityData, value.toInt())
        }
        return this
    }

    fun getShort(entityData: EntityData?): Short {
        return entityDataMap.getShort(entityData)
    }

    fun setString(entityData: EntityData?, value: String?): Metadata {
        val oldValue = getString(entityData)
        if (oldValue != value) {
            entityDataMap.putString(entityData, value)
        }
        return this
    }

    fun getString(entityData: EntityData?): String {
        return entityDataMap.getString(entityData)
    }

    fun setFloat(entityData: EntityData?, value: Float): Metadata {
        val oldValue = getFloat(entityData)
        if (oldValue != value) {
            entityDataMap.putFloat(entityData, value)
        }
        return this
    }

    fun getFloat(entityData: EntityData?): Float {
        return entityDataMap.getFloat(entityData)
    }

    fun setFlag(entityFlag: EntityFlag, value: Boolean): Metadata {
        val oldValue = getFlag(entityFlag)
        if (oldValue != value) {
            entityDataMap.getOrCreateFlags().setFlag(entityFlag, value)
        }
        return this
    }

    fun getInt(entityData: EntityData?): Int {
        return entityDataMap.getInt(entityData)
    }

    fun setInt(entityData: EntityData?, value: Int): Metadata {
        val oldValue = getInt(entityData)
        if (oldValue != value) {
            entityDataMap.putInt(entityData, value)
        }
        return this
    }

    fun getFlag(entityFlag: EntityFlag): Boolean {
        return entityDataMap.getOrCreateFlags().getFlag(entityFlag)
    }

    fun getEntityDataMap(): EntityDataMap {
        return entityDataMap
    }
}