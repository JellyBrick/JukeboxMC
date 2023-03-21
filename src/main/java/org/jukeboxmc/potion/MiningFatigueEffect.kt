package org.jukeboxmc.potion

import org.jukeboxmc.entity.EntityLiving
import java.awt.Color

/**
 * @author LucGamesYT
 * @version 1.0
 */
class MiningFatigueEffect : Effect() {
    override val id: Int
        get() = 4
    override val effectType: EffectType
        get() = EffectType.MINING_FATIGUE
    override val effectColor: Color
        get() = Color(74, 66, 23)

    override fun apply(entityLiving: EntityLiving) {}
    override fun update(currentTick: Long) {}
    override fun remove(entityLiving: EntityLiving) {}
}
