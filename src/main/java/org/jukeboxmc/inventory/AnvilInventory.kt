package org.jukeboxmc.inventory

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType
import org.jukeboxmc.item.Item
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class AnvilInventory(holder: InventoryHolder?) : ContainerInventory(holder, -1, 3) {
    override val inventoryHolder: InventoryHolder?
        get() = holder as Player
    override val type: InventoryType
        get() = InventoryType.ANVIL
    override val windowTypeId: ContainerType
        get() = ContainerType.ANVIL

    override fun setItem(slot: Int, item: Item, sendContent: Boolean) {
        super.setItem(slot - 1, item, sendContent)
    }

    override fun getItem(slot: Int): Item? {
        return super.getItem(slot - 1)
    }
}