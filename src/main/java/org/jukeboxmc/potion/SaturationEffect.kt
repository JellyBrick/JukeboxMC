package org.jukeboxmc.potion

import org.jukeboxmc.entity.EntityLiving
import java.awt.Color

/**
 * @author LucGamesYT
 * @version 1.0
 */
class SaturationEffect : Effect() {
    override val id: Int
        get() = 23
    override val effectType: EffectType
        get() = EffectType.SATURATION
    override val effectColor: Color
        get() = Color(255, 0, 255)

    override fun apply(entityLiving: EntityLiving) {}
    override fun update(currentTick: Long) {}
    override fun remove(entityLiving: EntityLiving) {}
}
