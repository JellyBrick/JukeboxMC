package org.jukeboxmc.item.enchantment

import java.lang.reflect.InvocationTargetException

/**
 * @author LucGamesYT
 * @version 1.0
 */
abstract class Enchantment {
    var level: Short = 0
        private set
    abstract val id: Short
    abstract val maxLevel: Int
    fun setLevel(level: Short): Enchantment {
        this.level = level
        return this
    }

    companion object {
        fun <T : Enchantment?> create(enchantmentType: EnchantmentType?): T {
            return try {
                EnchantmentRegistry.getEnchantmentClass(enchantmentType).getConstructor().newInstance() as T
            } catch (e: InstantiationException) {
                throw RuntimeException(e)
            } catch (e: IllegalAccessException) {
                throw RuntimeException(e)
            } catch (e: InvocationTargetException) {
                throw RuntimeException(e)
            } catch (e: NoSuchMethodException) {
                throw RuntimeException(e)
            }
        }
    }
}