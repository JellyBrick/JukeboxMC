package org.jukeboxmc.potion

import org.jukeboxmc.entity.EntityLiving
import java.awt.Color

/**
 * @author LucGamesYT
 * @version 1.0
 */
class WitherEffect : Effect() {
    override val id: Int
        get() = 20
    override val effectType: EffectType
        get() = EffectType.WITHER
    override val effectColor: Color
        get() = Color(53, 42, 39)

    override fun apply(entityLiving: EntityLiving) {}
    override fun update(currentTick: Long) {}
    override fun remove(entityLiving: EntityLiving) {}
}
