package org.jukeboxmc.potion

import org.jukeboxmc.entity.EntityLiving
import java.awt.Color

/**
 * @author LucGamesYT
 * @version 1.0
 */
class InstantDamageEffect : Effect() {
    override val id: Int
        get() = 7
    override val effectType: EffectType
        get() = EffectType.INSTANT_DAMAGE
    override val effectColor: Color
        get() = Color(67, 10, 9)

    override fun apply(entityLiving: EntityLiving) {}
    override fun update(currentTick: Long) {}
    override fun remove(entityLiving: EntityLiving) {}
}
