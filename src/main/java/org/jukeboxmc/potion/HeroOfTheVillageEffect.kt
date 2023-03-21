package org.jukeboxmc.potion

import java.awt.Color

/**
 * @author LucGamesYT
 * @version 1.0
 */
class HeroOfTheVillageEffect : Effect() {
    override val id: Int
        get() = 29
    override val effectType: EffectType
        get() = EffectType.HERO_OF_THE_VILLAGE
    override val effectColor: Color
        get() = Color(68, 255, 68)

    override fun apply(entityLiving: EntityLiving) {}
    override fun update(currentTick: Long) {}
    override fun remove(entityLiving: EntityLiving) {}
}