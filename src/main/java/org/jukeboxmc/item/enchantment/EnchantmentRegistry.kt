package org.jukeboxmc.item.enchantment

import java.lang.reflect.InvocationTargetException

/**
 * @author LucGamesYT
 * @version 1.0
 */
object EnchantmentRegistry {
    private val ENCHANTMENTS: MutableMap<EnchantmentType?, Class<out Enchantment>> = HashMap()
    private val ENCHANTMENT_BY_ID: MutableMap<Short, EnchantmentType> = HashMap()
    fun init() {
        register(EnchantmentType.AQUA_AFFINITY, EnchantmentAquaAffinity::class.java)
        register(EnchantmentType.BANE_OF_ARTHROPODS, EnchantmentBaneOfArthropods::class.java)
        register(EnchantmentType.BLAST_PROTECTION, EnchantmentBlastProtection::class.java)
        register(EnchantmentType.CHANNELING, EnchantmentChanneling::class.java)
        register(EnchantmentType.CURSE_OF_BINDING, EnchantmentCurseOfBinding::class.java)
        register(EnchantmentType.CURSE_OF_VANISHING, EnchantmentCurseOfVanishing::class.java)
        register(EnchantmentType.DEPTH_STRIDER, EnchantmentDepthStrider::class.java)
        register(EnchantmentType.EFFICIENCY, EnchantmentEfficiency::class.java)
        register(EnchantmentType.FEATHER_FALLING, EnchantmentFeatherFalling::class.java)
        register(EnchantmentType.FIRE_ASPECT, EnchantmentFireAspect::class.java)
        register(EnchantmentType.FIRE_PROTECTION, EnchantmentFireProtection::class.java)
        register(EnchantmentType.FLAME, EnchantmentFlame::class.java)
        register(EnchantmentType.FORTUNE, EnchantmentFortune::class.java)
        register(EnchantmentType.IMPALING, EnchantmentImpaling::class.java)
        register(EnchantmentType.INFINITY, EnchantmentInfinity::class.java)
        register(EnchantmentType.KNOCKBACK, EnchantmentKnockback::class.java)
        register(EnchantmentType.LOOTING, EnchantmentLooting::class.java)
        register(EnchantmentType.LOYALTY, EnchantmentLoyalty::class.java)
        register(EnchantmentType.LUCK_OF_THE_SEA, EnchantmentLuckOfTheSea::class.java)
        register(EnchantmentType.LURE, EnchantmentLure::class.java)
        register(EnchantmentType.MENDING, EnchantmentMending::class.java)
        register(EnchantmentType.MULTISHOT, EnchantmentMultishot::class.java)
        register(EnchantmentType.PIERCING, EnchantmentPiercing::class.java)
        register(EnchantmentType.POWER, EnchantmentPower::class.java)
        register(EnchantmentType.PROJECTILE_PROTECTION, EnchantmentProjectileProtection::class.java)
        register(EnchantmentType.PROTECTION, EnchantmentProtection::class.java)
        register(EnchantmentType.PUNCH, EnchantmentPunch::class.java)
        register(EnchantmentType.QUICK_CHARGE, EnchantmentQuickCharge::class.java)
        register(EnchantmentType.RESPIRATION, EnchantmentRespiration::class.java)
        register(EnchantmentType.RIPTIDE, EnchantmentRiptide::class.java)
        register(EnchantmentType.SHARPNESS, EnchantmentSharpness::class.java)
        register(EnchantmentType.SILK_TOUCH, EnchantmentSilkTouch::class.java)
        register(EnchantmentType.SMITE, EnchantmentSmite::class.java)
        register(EnchantmentType.SOUL_SPEED, EnchantmentSoulSpeed::class.java)
        register(EnchantmentType.THORNS, EnchantmentThorns::class.java)
        register(EnchantmentType.UNBREAKING, EnchantmentUnbreaking::class.java)
        register(EnchantmentType.SWIFT_SNEAK, EnchantmentSwiftSneak::class.java)
    }

    private fun register(enchantmentType: EnchantmentType, enchantmentClass: Class<out Enchantment>) {
        ENCHANTMENTS[enchantmentType] = enchantmentClass
        val enchantment = enchantmentClass.getConstructor().newInstance()
        ENCHANTMENT_BY_ID[enchantment.id] = enchantmentType
    }

    fun getEnchantmentType(id: Short): EnchantmentType? {
        return ENCHANTMENT_BY_ID[id]
    }

    fun getEnchantmentClass(enchantmentType: EnchantmentType?): Class<out Enchantment> {
        return ENCHANTMENTS[enchantmentType]!!
    }
}
