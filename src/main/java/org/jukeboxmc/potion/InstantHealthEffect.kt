package org.jukeboxmc.potion

import java.awt.Color

/**
 * @author LucGamesYT
 * @version 1.0
 */
class InstantHealthEffect : Effect() {
    override val id: Int
        get() = 6
    override val effectType: EffectType
        get() = EffectType.INSTANT_HEALTH
    override val effectColor: Color
        get() = Color(248, 36, 35)

    override fun apply(entityLiving: EntityLiving) {}
    override fun update(currentTick: Long) {}
    override fun remove(entityLiving: EntityLiving) {}
}