package org.jukeboxmc.inventory

import org.cloudburstmc.protocol.bedrock.packet.InventorySlotPacket
import org.jukeboxmc.item.Item
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class CursorInventory(holder: InventoryHolder) : Inventory(holder, 1) {
    override val inventoryHolder: InventoryHolder
        get() = holder as Player
    override val type: InventoryType
        get() = InventoryType.CURSOR

    override fun sendContents(player: Player) {
        this.sendContents(0, player)
    }

    override fun sendContents(slot: Int, player: Player) {
        val inventorySlotPacket = InventorySlotPacket()
        inventorySlotPacket.containerId = WindowId.CURSOR_DEPRECATED.id
        inventorySlotPacket.item = contents[slot].toItemData()
        inventorySlotPacket.slot = slot
        player.playerConnection.sendPacket(inventorySlotPacket)
    }

    fun setItem(item: Item) {
        this.setItem(0, item)
    }

    val item: Item
        get() = getItem(0)
}
