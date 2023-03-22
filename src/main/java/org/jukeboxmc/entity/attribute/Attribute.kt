package org.jukeboxmc.entity.attribute

import com.nukkitx.protocol.bedrock.data.AttributeData

/**
 * @author LucGamesYT
 * @version 1.0
 */
data class Attribute(val key: String, var minValue: Float, var maxValue: Float, private var currentValue: Float) :
    Cloneable {
    private val defaultValue: Float
    private var dirty: Boolean

    init {
        defaultValue = currentValue
        dirty = true
    }

    fun getCurrentValue(): Float {
        return currentValue
    }

    fun setCurrentValue(currentValue: Float) {
        this.currentValue = currentValue
        dirty = true
    }

    fun isDirty(): Boolean {
        val value = dirty
        dirty = false
        return value
    }

    fun reset() {
        currentValue = defaultValue
        dirty = true
    }

    fun toNetwork(): AttributeData {
        return AttributeData(key, minValue, maxValue, currentValue, defaultValue)
    }

    @Throws(CloneNotSupportedException::class)
    override fun clone(): Attribute {
        return super.clone() as Attribute
    }
}
