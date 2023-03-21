package org.jukeboxmc.potion

import java.awt.Color

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlindnessEffect : Effect() {
    override val id: Int
        get() = 15
    override val effectType: EffectType
        get() = EffectType.BLINDNESS
    override val effectColor: Color
        get() = Color(191, 192, 192)

    override fun apply(entityLiving: EntityLiving) {}
    override fun update(currentTick: Long) {}
    override fun remove(entityLiving: EntityLiving) {}
}