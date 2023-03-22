package org.jukeboxmc.potion

import org.jukeboxmc.entity.EntityLiving
import org.jukeboxmc.entity.attribute.AttributeType
import java.awt.Color

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
        entityLiving.movement = (amplifier + 1) * 0.2f
    }

    override fun remove(entityLiving: EntityLiving) {
        entityLiving.getAttribute(AttributeType.MOVEMENT).reset()
    }
}
