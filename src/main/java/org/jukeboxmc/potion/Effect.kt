package org.jukeboxmc.potion

import org.jukeboxmc.entity.EntityLiving
import java.awt.Color
import java.util.concurrent.TimeUnit

/**
 * @author LucGamesYT
 * @version 1.0
 */
abstract class Effect {
    var amplifier = 0
        protected set
    var isVisible = true
        protected set
    var duration = 0
        protected set
    abstract val id: Int
    abstract val effectType: EffectType
    abstract val effectColor: Color
    abstract fun apply(entityLiving: EntityLiving)
    open fun canExecute(): Boolean {
        return false
    }

    abstract fun update(currentTick: Long)
    abstract fun remove(entityLiving: EntityLiving)
    fun setAmplifier(amplifier: Int): Effect {
        this.amplifier = amplifier
        return this
    }

    fun setVisible(visible: Boolean): Effect {
        isVisible = visible
        return this
    }

    fun setDuration(duration: Int): Effect {
        this.duration = duration
        return this
    }

    fun setDuration(duration: Int, timeUnit: TimeUnit): Effect {
        this.duration = (timeUnit.toMillis(duration.toLong()) / 50).toInt()
        return this
    }

    val color: IntArray
        get() {
            val color = effectColor
            return intArrayOf(color.red, color.green, color.blue)
        }

    companion object {
        fun createEffect(effectType: EffectType): Effect {
            return EffectRegistry.getEffectClass(effectType).getConstructor().newInstance()
        }

        inline fun <reified T : Effect> create(effectType: EffectType): T =
            createEffect(effectType) as T
    }
}
