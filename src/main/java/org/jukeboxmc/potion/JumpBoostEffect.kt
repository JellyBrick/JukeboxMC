package org.jukeboxmc.potion

import java.awt.Color

/**
 * @author LucGamesYT
 * @version 1.0
 */
class JumpBoostEffect : Effect() {
    override val id: Int
        get() = 8
    override val effectType: EffectType
        get() = EffectType.JUMP_BOOST
    override val effectColor: Color
        get() = Color(34, 255, 76)

    override fun apply(entityLiving: EntityLiving) {}
    override fun update(currentTick: Long) {}
    override fun remove(entityLiving: EntityLiving) {}
}