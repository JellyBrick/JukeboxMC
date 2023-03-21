package org.jukeboxmc.potion

import org.jukeboxmc.entity.EntityLiving
import org.jukeboxmc.event.entity.EntityDamageEvent
import org.jukeboxmc.event.entity.EntityDamageEvent.DamageSource
import java.awt.Color

/**
 * @author LucGamesYT
 * @version 1.0
 */
class FatalPoison : Effect() {
    override val id: Int
        get() = 25
    override val effectType: EffectType
        get() = EffectType.FATAL_POISON
    override val effectColor: Color
        get() = Color(78, 147, 49)

    override fun apply(entityLiving: EntityLiving) {
        if (canExecute()) {
            if (entityLiving.health > 2) {
                entityLiving.damage(EntityDamageEvent(entityLiving, 1f, DamageSource.MAGIC_EFFECT))
            }
        }
    }

    override fun canExecute(): Boolean {
        var interval: Int
        return if ((25 shr amplifier).also { interval = it } > 0) {
            duration % interval == 0
        } else {
            false
        }
    }

    override fun update(currentTick: Long) {}
    override fun remove(entityLiving: EntityLiving) {}
}
