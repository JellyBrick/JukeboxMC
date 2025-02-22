package org.jukeboxmc.inventory

import org.jukeboxmc.item.Item
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BigCraftingGridInventory(holder: InventoryHolder?) : CraftingGridInventory(holder, -1, 9) {
    override val inventoryHolder: InventoryHolder
        get() = holder as Player
    override val type: InventoryType
        get() = InventoryType.BIG_CRAFTING_GRID

    override fun setItem(slot: Int, item: Item, sendContent: Boolean) {
        super.setItem(slot - offset, item, sendContent)
    }

    override fun getItem(slot: Int): Item {
        return super.getItem(slot - offset)
    }

    override val offset: Int
        get() = 32
}
