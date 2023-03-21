package org.jukeboxmc.potion

import java.awt.Color

/**
 * @author LucGamesYT
 * @version 1.0
 */
class StrengthEffect : Effect() {
    override val id: Int
        get() = 5
    override val effectType: EffectType
        get() = EffectType.STRENGTH
    override val effectColor: Color
        get() = Color(147, 36, 35)

    override fun apply(entityLiving: EntityLiving) {}
    override fun update(currentTick: Long) {}
    override fun remove(entityLiving: EntityLiving) {}
}