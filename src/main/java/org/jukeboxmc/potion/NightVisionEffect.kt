package org.jukeboxmc.potion

import java.awt.Color

/**
 * @author LucGamesYT
 * @version 1.0
 */
class NightVisionEffect : Effect() {
    override val id: Int
        get() = 16
    override val effectType: EffectType
        get() = EffectType.NIGHT_VISION
    override val effectColor: Color
        get() = Color(0, 0, 139)

    override fun apply(entityLiving: EntityLiving) {}
    override fun update(currentTick: Long) {}
    override fun remove(entityLiving: EntityLiving) {}
}