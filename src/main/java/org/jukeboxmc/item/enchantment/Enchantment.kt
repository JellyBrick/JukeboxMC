package org.jukeboxmc.item.enchantment

/**
 * @author LucGamesYT
 * @version 1.0
 */
abstract class Enchantment {
    var level: Short = 0
    abstract val id: Short
    abstract val maxLevel: Int

    companion object {
        fun createEnchantment(enchantmentType: EnchantmentType): Enchantment {
            return EnchantmentRegistry.getEnchantmentClass(enchantmentType).getConstructor().newInstance()
        }

        inline fun <reified T : Enchantment> create(enchantmentType: EnchantmentType): T =
            createEnchantment(enchantmentType) as T
    }
}
