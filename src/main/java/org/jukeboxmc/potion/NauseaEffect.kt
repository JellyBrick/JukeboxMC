package org.jukeboxmc.potion

import java.awt.Color

/**
 * @author LucGamesYT
 * @version 1.0
 */
class NauseaEffect : Effect() {
    override val id: Int
        get() = 9
    override val effectType: EffectType
        get() = EffectType.NAUSEA
    override val effectColor: Color
        get() = Color(85, 29, 74)

    override fun apply(entityLiving: EntityLiving) {}
    override fun update(currentTick: Long) {}
    override fun remove(entityLiving: EntityLiving) {}
}