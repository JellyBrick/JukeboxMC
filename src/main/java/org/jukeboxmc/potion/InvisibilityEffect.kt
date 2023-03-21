package org.jukeboxmc.potion

import java.awt.Color

/**
 * @author LucGamesYT
 * @version 1.0
 */
class InvisibilityEffect : Effect() {
    override val id: Int
        get() = 14
    override val effectType: EffectType
        get() = EffectType.INVISIBILITY
    override val effectColor: Color
        get() = Color(127, 131, 146)

    override fun apply(entityLiving: EntityLiving) {
        entityLiving.updateMetadata(entityLiving.getMetadata().setFlag(EntityFlag.INVISIBLE, true))
    }

    override fun update(currentTick: Long) {}
    override fun remove(entityLiving: EntityLiving) {
        entityLiving.updateMetadata(entityLiving.getMetadata().setFlag(EntityFlag.INVISIBLE, false))
    }
}