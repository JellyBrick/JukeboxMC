package org.jukeboxmc.inventory

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType
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
        inventoryContentPacket.setContainerId(WindowId.OPEN_CONTAINER.id)
        inventoryContentPacket.setContents(this.itemDataContents)
        player.playerConnection.sendPacket(inventoryContentPacket)
    }

    override fun sendContents(slot: Int, player: Player) {
        val inventorySlotPacket = InventorySlotPacket()
        inventorySlotPacket.setContainerId(WindowId.OPEN_CONTAINER.id)
        inventorySlotPacket.setSlot(slot)
        inventorySlotPacket.setItem(this.contents[slot].toItemData())
        player.playerConnection.sendPacket(inventorySlotPacket)
    }

    open fun addViewer(player: Player, position: Vector, windowId: Byte) {
        val containerOpenPacket = ContainerOpenPacket()
        containerOpenPacket.setUniqueEntityId(holderId)
        containerOpenPacket.setId(windowId)
        containerOpenPacket.setType(windowTypeId)
        containerOpenPacket.setBlockPosition(position.toVector3i())
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