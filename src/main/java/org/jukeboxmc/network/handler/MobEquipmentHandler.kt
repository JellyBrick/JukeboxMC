package org.jukeboxmc.network.handler

import com.nukkitx.protocol.bedrock.packet.MobEquipmentPacket
import org.jukeboxmc.Server
import org.jukeboxmc.inventory.Inventory
import org.jukeboxmc.inventory.PlayerInventory
import org.jukeboxmc.inventory.WindowId
import org.jukeboxmc.item.Item
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class MobEquipmentHandler : PacketHandler<MobEquipmentPacket> {
    override fun handle(packet: MobEquipmentPacket, server: Server, player: Player) {
        val inventory = getInventory(player, WindowId.getWindowIdById(packet.containerId))
        val item = inventory.getItem(packet.hotbarSlot)
        if (item != Item(packet.item, false)) {
            inventory.sendContents(player)
            return
        }
        if (inventory is PlayerInventory) {
            inventory.setItemInHandSlot(packet.hotbarSlot)
            player.setAction(false)
        }
    }

    private fun getInventory(player: Player, windowId: WindowId?): Inventory {
        return when (windowId ?: WindowId.INVENTORY) {
            WindowId.PLAYER -> player.inventory
            WindowId.CURSOR_DEPRECATED, WindowId.CURSOR -> player.getCursorInventory()
            WindowId.ARMOR_DEPRECATED, WindowId.ARMOR -> player.getArmorInventory()
            else -> player.getCurrentInventory()!!
        }
    }
}
