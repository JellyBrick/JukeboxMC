package org.jukeboxmc.potion

import java.awt.Color

/**
 * @author LucGamesYT
 * @version 1.0
 */
class WaterBreathingEffect : Effect() {
    override val id: Int
        get() = 13
    override val effectType: EffectType
        get() = EffectType.WATER_BREATHING
    override val effectColor: Color
        get() = Color(46, 82, 153)

    override fun apply(entityLiving: EntityLiving) {}
    override fun update(currentTick: Long) {}
    override fun remove(entityLiving: EntityLiving) {}
}