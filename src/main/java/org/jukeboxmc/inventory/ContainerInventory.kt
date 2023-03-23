package org.jukeboxmc.inventory

import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerType
import org.cloudburstmc.protocol.bedrock.packet.ContainerOpenPacket
import org.cloudburstmc.protocol.bedrock.packet.InventoryContentPacket
import org.cloudburstmc.protocol.bedrock.packet.InventorySlotPacket
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
abstract class ContainerInventory : Inventory {
    constructor(holder: InventoryHolder, size: Int) : super(holder, size)
    constructor(holder: InventoryHolder?, holderId: Int, size: Int) : super(holder, holderId, size)

    override val windowTypeId: ContainerType
        get() = ContainerType.CONTAINER

    override fun sendContents(player: Player) {
        val inventoryContentPacket = InventoryContentPacket()
        inventoryContentPacket.containerId = WindowId.OPEN_CONTAINER.id
        inventoryContentPacket.contents = this.itemDataContents
        player.playerConnection.sendPacket(inventoryContentPacket)
    }

    override fun sendContents(slot: Int, player: Player) {
        val inventorySlotPacket = InventorySlotPacket()
        inventorySlotPacket.containerId = WindowId.OPEN_CONTAINER.id
        inventorySlotPacket.slot = slot
        inventorySlotPacket.item = this.contents[slot].toItemData()
        player.playerConnection.sendPacket(inventorySlotPacket)
    }

    open fun addViewer(player: Player, position: Vector, windowId: Byte) {
        val containerOpenPacket = ContainerOpenPacket()
        containerOpenPacket.uniqueEntityId = holderId
        containerOpenPacket.id = windowId
        containerOpenPacket.type = windowTypeId
        containerOpenPacket.blockPosition = position.toVector3i()
        player.playerConnection.sendPacket(containerOpenPacket)
        super.addViewer(player)
        onOpen(player)
    }

    override fun removeViewer(player: Player) {
        super.removeViewer(player)
        onClose(player)
    }

    open fun onOpen(player: Player) {
        this.sendContents(player)
    }

    open fun onClose(player: Player) {}
}
