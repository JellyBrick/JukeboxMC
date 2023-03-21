package org.jukeboxmc.potion

import java.awt.Color

/**
 * @author LucGamesYT
 * @version 1.0
 */
class PoisonEffect : Effect() {
    override val id: Int
        get() = 19
    override val effectType: EffectType
        get() = EffectType.POISON
    override val effectColor: Color
        get() = Color(78, 147, 49)

    override fun apply(entityLiving: EntityLiving) {}
    override fun update(currentTick: Long) {}
    override fun remove(entityLiving: EntityLiving) {}
}