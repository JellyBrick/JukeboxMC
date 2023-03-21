package org.jukeboxmc.potion

import org.jukeboxmc.entity.EntityLiving
import java.awt.Color

/**
 * @author LucGamesYT
 * @version 1.0
 */
class AbsorptionEffect : Effect() {
    override val id: Int
        get() = 22
    override val effectType: EffectType
        get() = EffectType.ABSORPTION
    override val effectColor: Color
        get() = Color(36, 107, 251)

    override fun apply(entityLiving: EntityLiving) {
        val value = (amplifier + 1) * 4
        if (value > entityLiving.absorption) {
            entityLiving.absorption = value.toFloat()
        }
    }

    override fun update(currentTick: Long) {}
    override fun remove(entityLiving: EntityLiving) {
        entityLiving.absorption = 0f
    }
}
