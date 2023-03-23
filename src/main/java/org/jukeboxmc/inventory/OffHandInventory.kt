package org.jukeboxmc.inventory

import org.cloudburstmc.protocol.bedrock.packet.InventoryContentPacket
import org.cloudburstmc.protocol.bedrock.packet.InventorySlotPacket
import org.jukeboxmc.item.Item
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class OffHandInventory(holder: InventoryHolder) : Inventory(holder, 1) {
    override fun sendContents(player: Player) {
        val inventoryContentPacket = InventoryContentPacket()
        inventoryContentPacket.containerId = WindowId.OFFHAND_DEPRECATED.id
        inventoryContentPacket.contents = this.itemDataContents
        player.playerConnection.sendPacket(inventoryContentPacket)
    }

    override fun sendContents(slot: Int, player: Player) {
        val inventorySlotPacket = InventorySlotPacket()
        inventorySlotPacket.containerId = WindowId.OFFHAND_DEPRECATED.id
        inventorySlotPacket.item = contents[slot].toItemData()
        inventorySlotPacket.slot = slot
        player.playerConnection.sendPacket(inventorySlotPacket)
    }

    override val inventoryHolder: InventoryHolder
        get() = holder as Player
    override val type: InventoryType
        get() = InventoryType.OFFHAND

    override fun setItem(slot: Int, item: Item, sendContent: Boolean) {
        super.setItem(slot - 1, item, sendContent)
    }

    override fun getItem(slot: Int): Item {
        return super.getItem(slot - 1)
    }
}
