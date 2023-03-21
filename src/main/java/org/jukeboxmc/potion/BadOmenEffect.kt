package org.jukeboxmc.potion

import java.awt.Color

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BadOmenEffect : Effect() {
    override val id: Int
        get() = 28
    override val effectType: EffectType
        get() = EffectType.BAD_OMEN
    override val effectColor: Color
        get() = Color(11, 97, 56)

    override fun apply(entityLiving: EntityLiving) {}
    override fun update(currentTick: Long) {}
    override fun remove(entityLiving: EntityLiving) {}
}