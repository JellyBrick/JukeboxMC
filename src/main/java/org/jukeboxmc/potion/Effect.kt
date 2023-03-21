package org.jukeboxmc.potion

import java.awt.Color
import java.lang.reflect.InvocationTargetException
import java.util.concurrent.TimeUnit
import lombok.ToString

/**
 * @author LucGamesYT
 * @version 1.0
 */
@ToString
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
        fun <T : Effect?> create(effectType: EffectType?): T {
            return try {
                EffectRegistry.getEffectClass(effectType).getConstructor().newInstance() as T
            } catch (e: InstantiationException) {
                throw RuntimeException(e)
            } catch (e: IllegalAccessException) {
                throw RuntimeException(e)
            } catch (e: InvocationTargetException) {
                throw RuntimeException(e)
            } catch (e: NoSuchMethodException) {
                throw RuntimeException(e)
            }
        }
    }
}