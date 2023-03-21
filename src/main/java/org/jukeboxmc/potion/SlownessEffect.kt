package org.jukeboxmc.potion

import org.jukeboxmc.entity.EntityLiving
import java.awt.Color

/**
 * @author LucGamesYT
 * @version 1.0
 */
class SlownessEffect : Effect() {
    override val id: Int
        get() = 2
    override val effectType: EffectType
        get() = EffectType.SLOWNESS
    override val effectColor: Color
        get() = Color(90, 108, 129)

    override fun update(currentTick: Long) {}
    override fun apply(entityLiving: EntityLiving) {
        entityLiving.movement = (entityLiving.movement * (1 - 0.15f * (amplifier + 1)))
    }

    override fun remove(entityLiving: EntityLiving) {
        entityLiving.movement = (entityLiving.movement / (1 - 0.15f * (amplifier + 1)))
    }
}
