package org.jukeboxmc.potion

import org.jukeboxmc.entity.EntityLiving
import java.awt.Color

/**
 * @author LucGamesYT
 * @version 1.0
 */
class LevitationEffect : Effect() {
    override val id: Int
        get() = 24
    override val effectType: EffectType
        get() = EffectType.LEVITATION
    override val effectColor: Color
        get() = Color(78, 147, 49)

    override fun apply(entityLiving: EntityLiving) {}
    override fun update(currentTick: Long) {}
    override fun remove(entityLiving: EntityLiving) {}
}
