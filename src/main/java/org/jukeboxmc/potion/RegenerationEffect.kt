package org.jukeboxmc.potion

import org.jukeboxmc.entity.EntityLiving
import org.jukeboxmc.event.entity.EntityHealEvent
import java.awt.Color

/**
 * @author LucGamesYT
 * @version 1.0
 */
class RegenerationEffect : Effect() {
    override val id: Int
        get() = 10
    override val effectType: EffectType
        get() = EffectType.REGENERATION
    override val effectColor: Color
        get() = Color(205, 92, 171)

    override fun apply(entityLiving: EntityLiving) {
        if (canExecute()) {
            entityLiving.setHeal(1f, EntityHealEvent.Cause.REGENERATION_EFFECT)
        }
    }

    override fun canExecute(): Boolean {
        var interval: Int
        return if ((40 shr amplifier).also { interval = it } > 0) {
            duration % interval == 0
        } else {
            false
        }
    }

    override fun update(currentTick: Long) {}
    override fun remove(entityLiving: EntityLiving) {}
}
