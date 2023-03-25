package org.jukeboxmc.inventory

import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerType
import org.cloudburstmc.protocol.bedrock.packet.InventoryContentPacket
import org.cloudburstmc.protocol.bedrock.packet.InventorySlotPacket
import org.cloudburstmc.protocol.bedrock.packet.MobEquipmentPacket
import org.jukeboxmc.entity.passiv.EntityHuman
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.player.Player
import java.util.Objects

/**
 * @author LucGamesYT
 * @version 1.0
 */
class PlayerInventory(holder: InventoryHolder) : ContainerInventory(holder, 36) {
    private var itemInHandSlot = 0
    override val type: InventoryType
        get() = InventoryType.PLAYER
    override val windowTypeId: ContainerType
        get() = ContainerType.INVENTORY
    override val inventoryHolder: InventoryHolder
        get() = holder as Player

    override fun sendContents(player: Player) {
        val inventoryContentPacket = InventoryContentPacket()
        if (player.currentInventory === this) {
            inventoryContentPacket.containerId = WindowId.OPEN_CONTAINER.id
            inventoryContentPacket.contents = this.itemDataContents
            player.playerConnection.sendPacket(inventoryContentPacket)
            return
        }
        inventoryContentPacket.containerId = WindowId.PLAYER.id
        inventoryContentPacket.contents = this.itemDataContents
        player.playerConnection.sendPacket(inventoryContentPacket)
    }

    override fun sendContents(slot: Int, player: Player) {
        if (player.currentInventory != null && player.currentInventory === this) {
            val inventorySlotPacket = InventorySlotPacket()
            inventorySlotPacket.slot = slot
            inventorySlotPacket.item = contents[slot].toItemData()
            inventorySlotPacket.containerId = WindowId.OPEN_CONTAINER.id
            player.playerConnection.sendPacket(inventorySlotPacket)
        }
        val inventorySlotPacket = InventorySlotPacket()
        inventorySlotPacket.slot = slot
        inventorySlotPacket.item = contents[slot].toItemData()
        inventorySlotPacket.containerId = WindowId.PLAYER.id
        player.playerConnection.sendPacket(inventorySlotPacket)
    }

    override fun removeViewer(player: Player) {
        if (player !== holder) {
            super.removeViewer(player)
        }
    }

    override fun setItem(slot: Int, item: Item) {
        val oldItem = getItem(slot)
        super.setItem(slot, item)
        if (slot == itemInHandSlot && holder is Player) {
            val player = holder as Player
            oldItem.removeFromHand(player)
            item.addToHand(player)
            updateItemInHandForAll()
        }
    }

    var itemInHand: Item
        get() {
            val content = contents[itemInHandSlot]
            return Objects.requireNonNullElseGet(content) { Item.create<Item>(ItemType.AIR) }
        }
        set(itemInHand) {
            this.setItem(itemInHandSlot, itemInHand)
            sendItemInHand()
        }

    fun getItemInHandSlot(): Int {
        return itemInHandSlot
    }

    fun setItemInHandSlot(itemInHandSlot: Int) {
        if (itemInHandSlot in 0..8) {
            val oldItem = itemInHand
            oldItem.removeFromHand((holder as Player))
            this.itemInHandSlot = itemInHandSlot
            val item = itemInHand
            item.addToHand((holder as Player))
            updateItemInHandForAll()
        }
    }

    fun createMobEquipmentPacket(entityHuman: EntityHuman): MobEquipmentPacket {
        val mobEquipmentPacket = MobEquipmentPacket()
        mobEquipmentPacket.runtimeEntityId = entityHuman.entityId
        mobEquipmentPacket.item = itemInHand.toItemData()
        mobEquipmentPacket.containerId = WindowId.PLAYER.id
        mobEquipmentPacket.hotbarSlot = itemInHandSlot
        mobEquipmentPacket.inventorySlot = itemInHandSlot
        return mobEquipmentPacket
    }

    fun sendItemInHand() {
        if (holder is Player) {
            val player = holder as Player
            player.playerConnection.sendPacket(createMobEquipmentPacket(player))
            this.sendContents(itemInHandSlot, player)
        }
    }

    fun updateItemInHandForAll() {
        if (holder is EntityHuman) {
            val entityHuman = holder as EntityHuman
            val mobEquipmentPacket: MobEquipmentPacket = createMobEquipmentPacket(entityHuman)
            for (onlinePlayers in entityHuman.world.players) {
                if (onlinePlayers !== entityHuman) {
                    onlinePlayers.playerConnection.sendPacket(mobEquipmentPacket)
                }
            }
        }
    }
}
