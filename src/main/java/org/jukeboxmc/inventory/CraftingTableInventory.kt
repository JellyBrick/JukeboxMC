package org.jukeboxmc.inventory

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType
import org.jukeboxmc.item.Item
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class CraftingTableInventory(holder: InventoryHolder?) : ContainerInventory(holder, -1, 9) {
    override val inventoryHolder: InventoryHolder?
        get() = holder as Player
    override val type: InventoryType
        get() = InventoryType.WORKBENCH
    override val windowTypeId: ContainerType
        get() = ContainerType.WORKBENCH

    override fun onOpen(player: Player) {
        super.onOpen(player)
        player.craftingGridInventory = BigCraftingGridInventory(player)
    }

    override fun onClose(player: Player) {
        player.craftingGridInventory = SmallCraftingGridInventory(player)
    }

    override fun setItem(slot: Int, item: Item, sendContent: Boolean) {
        super.setItem(slot - 32, item, sendContent)
    }

    override fun getItem(slot: Int): Item? {
        return super.getItem(slot - 32)
    }
}