package org.jukeboxmc.block.behavior

import org.cloudburstmc.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.enchantment.EnchantmentType
import org.jukeboxmc.util.Identifier
import java.util.concurrent.ThreadLocalRandom

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockGoldOre : Block {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun getDrops(item: Item?): List<Item> {
        if (item != null && isCorrectToolType(item) && isCorrectTierType(item)) {
            var amount = 1
            val enchantment = item.getEnchantment(EnchantmentType.FORTUNE)
            if (enchantment != null) {
                amount += ThreadLocalRandom.current().nextInt(0, enchantment.level + 1)
            }
            return listOf(Item.create<Item>(ItemType.RAW_GOLD).setAmount(amount))
        }
        return emptyList()
    }
}
