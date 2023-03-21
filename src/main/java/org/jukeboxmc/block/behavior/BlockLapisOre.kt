package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import java.util.concurrent.ThreadLocalRandom
import org.jukeboxmc.block.Block
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.enchantment.EnchantmentType
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockLapisOre : Block {
    constructor(identifier: Identifier?) : super(identifier)
    constructor(identifier: Identifier?, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun getDrops(item: Item?): List<Item> {
        if (item != null && isCorrectToolType(item) && isCorrectTierType(item)) {
            val random = ThreadLocalRandom.current()
            var amount = 2 + random.nextInt(5)
            val enchantment = item.getEnchantment(EnchantmentType.FORTUNE)
            if (enchantment != null) {
                amount += random.nextInt(enchantment.level + 2) - 1
            }
            return listOf(Item.Companion.create<Item>(ItemType.LAPIS_LAZULI).setAmount(amount))
        }
        return emptyList()
    }
}