package org.jukeboxmc.potion

import java.awt.Color

/**
 * @author LucGamesYT
 * @version 1.0
 */
class HealthBoostEffect : Effect() {
    override val id: Int
        get() = 21
    override val effectType: EffectType
        get() = EffectType.HEALTH_BOOST
    override val effectColor: Color
        get() = Color(248, 125, 35)

    override fun apply(entityLiving: EntityLiving) {}
    override fun update(currentTick: Long) {}
    override fun remove(entityLiving: EntityLiving) {}
}