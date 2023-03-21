package org.jukeboxmc.network.handler

import org.jukeboxmc.Server
import org.jukeboxmc.inventory.Inventory
import org.jukeboxmc.item.Item
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class MobEquipmentHandler : PacketHandler<MobEquipmentPacket> {
    override fun handle(packet: MobEquipmentPacket, server: Server, player: Player) {
        val inventory = getInventory(player, WindowId.Companion.getWindowIdById(packet.getContainerId()))
        if (inventory != null) {
            val item = inventory.getItem(packet.getHotbarSlot())
            if (item != Item(packet.getItem(), false)) {
                inventory.sendContents(player)
                return
            }
            if (inventory is PlayerInventory) {
                inventory.setItemInHandSlot(packet.getHotbarSlot())
                player.setAction(false)
            }
        }
    }

    private fun getInventory(player: Player, windowId: WindowId?): Inventory {
        return when (if (windowId != null) windowId else WindowId.INVENTORY) {
            WindowId.PLAYER -> player.inventory
            WindowId.CURSOR_DEPRECATED, WindowId.CURSOR -> player.cursorInventory
            WindowId.ARMOR_DEPRECATED, WindowId.ARMOR -> player.armorInventory
            else -> player.currentInventory
        }
    }
}