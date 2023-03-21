package org.jukeboxmc.potion

import org.jukeboxmc.entity.EntityLiving
import java.awt.Color

/**
 * @author LucGamesYT
 * @version 1.0
 */
class DarknessEffect : Effect() {
    override val id: Int
        get() = 30
    override val effectType: EffectType
        get() = EffectType.DARKNESS
    override val effectColor: Color
        get() = Color(41, 39, 33)

    override fun apply(entityLiving: EntityLiving) {}
    override fun update(currentTick: Long) {}
    override fun remove(entityLiving: EntityLiving) {}
}
