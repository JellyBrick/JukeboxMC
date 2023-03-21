package org.jukeboxmc.potion

import java.awt.Color

/**
 * @author LucGamesYT
 * @version 1.0
 */
class FireResistanceEffect : Effect() {
    override val id: Int
        get() = 12
    override val effectType: EffectType
        get() = EffectType.FIRE_RESISTANCE
    override val effectColor: Color
        get() = Color(228, 154, 58)

    override fun apply(entityLiving: EntityLiving) {}
    override fun update(currentTick: Long) {}
    override fun remove(entityLiving: EntityLiving) {}
}