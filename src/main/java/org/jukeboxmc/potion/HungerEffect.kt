package org.jukeboxmc.potion

import java.awt.Color

/**
 * @author LucGamesYT
 * @version 1.0
 */
class HungerEffect : Effect() {
    override val id: Int
        get() = 17
    override val effectType: EffectType
        get() = EffectType.HUNGER
    override val effectColor: Color
        get() = Color(46, 139, 87)

    override fun apply(entityLiving: EntityLiving) {}
    override fun update(currentTick: Long) {}
    override fun remove(entityLiving: EntityLiving) {}
}