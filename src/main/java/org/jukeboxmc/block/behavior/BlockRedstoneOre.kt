package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
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
class BlockRedstoneOre : Block {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun getDrops(item: Item?): List<Item> {
        if (item != null && isCorrectToolType(item) && isCorrectTierType(item)) {
            val random = ThreadLocalRandom.current()
            var amount = 4 + random.nextInt(2)
            val enchantment = item.getEnchantment(EnchantmentType.FORTUNE)
            if (enchantment != null) {
                amount += random.nextInt(enchantment.level + 1)
            }
            return listOf(Item.create<Item>(ItemType.LAPIS_LAZULI).setAmount(amount))
        }
        return emptyList()
    }
}
