package org.jukeboxmc.potion

import java.awt.Color
import org.jukeboxmc.entity.attribute.AttributeType

/**
 * @author LucGamesYT
 * @version 1.0
 */
class SpeedEffect : Effect() {
    override val id: Int
        get() = 1
    override val effectType: EffectType
        get() = EffectType.SPEED
    override val effectColor: Color
        get() = Color(124, 175, 198)

    override fun update(currentTick: Long) {}
    override fun apply(entityLiving: EntityLiving) {
        entityLiving.setMovement((amplifier + 1) * 0.2f)
    }

    override fun remove(entityLiving: EntityLiving) {
        entityLiving.getAttribute(AttributeType.MOVEMENT).reset()
    }
}