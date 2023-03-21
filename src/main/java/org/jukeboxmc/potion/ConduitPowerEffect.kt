package org.jukeboxmc.potion

import org.jukeboxmc.entity.EntityLiving
import java.awt.Color

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ConduitPowerEffect : Effect() {
    override val id: Int
        get() = 26
    override val effectType: EffectType
        get() = EffectType.CONDUIT_POWER
    override val effectColor: Color
        get() = Color(29, 194, 209)

    override fun apply(entityLiving: EntityLiving) {}
    override fun update(currentTick: Long) {}
    override fun remove(entityLiving: EntityLiving) {}
}
