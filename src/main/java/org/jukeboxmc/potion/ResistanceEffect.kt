package org.jukeboxmc.potion

import java.awt.Color

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ResistanceEffect : Effect() {
    override val id: Int
        get() = 11
    override val effectType: EffectType
        get() = EffectType.RESISTANCE
    override val effectColor: Color
        get() = Color(153, 69, 58)

    override fun apply(entityLiving: EntityLiving) {}
    override fun update(currentTick: Long) {}
    override fun remove(entityLiving: EntityLiving) {}
}