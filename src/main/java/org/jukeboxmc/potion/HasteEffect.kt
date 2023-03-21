package org.jukeboxmc.potion

import org.jukeboxmc.entity.EntityLiving
import java.awt.Color

/**
 * @author LucGamesYT
 * @version 1.0
 */
class HasteEffect : Effect() {
    override val id: Int
        get() = 3
    override val effectType: EffectType
        get() = EffectType.HASTE
    override val effectColor: Color
        get() = Color(217, 192, 67)

    override fun update(currentTick: Long) {}
    override fun apply(entityLiving: EntityLiving) {}
    override fun remove(entityLiving: EntityLiving) {}
}
