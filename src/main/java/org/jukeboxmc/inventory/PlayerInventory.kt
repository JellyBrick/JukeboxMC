package org.jukeboxmc.inventory

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType
import java.util.Objects
import java.util.function.Supplier
import org.jukeboxmc.entity.passiv.EntityHuman
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.player.Player

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
    override val inventoryHolder: InventoryHolder?
        get() = holder as Player

    override fun sendContents(player: Player) {
        val inventoryContentPacket = InventoryContentPacket()
        if (player.currentInventory === this) {
            inventoryContentPacket.setContainerId(WindowId.OPEN_CONTAINER.id)
            inventoryContentPacket.setContents(this.itemDataContents)
            player.playerConnection.sendPacket(inventoryContentPacket)
            return
        }
        inventoryContentPacket.setContainerId(WindowId.PLAYER.id)
        inventoryContentPacket.setContents(this.itemDataContents)
        player.playerConnection.sendPacket(inventoryContentPacket)
    }

    override fun sendContents(slot: Int, player: Player) {
        if (player.currentInventory != null && player.currentInventory === this) {
            val inventorySlotPacket = InventorySlotPacket()
            inventorySlotPacket.setSlot(slot)
            inventorySlotPacket.setItem(content[slot].toItemData())
            inventorySlotPacket.setContainerId(WindowId.OPEN_CONTAINER.id)
            player.playerConnection.sendPacket(inventorySlotPacket)
        }
        val inventorySlotPacket = InventorySlotPacket()
        inventorySlotPacket.setSlot(slot)
        inventorySlotPacket.setItem(content[slot].toItemData())
        inventorySlotPacket.setContainerId(WindowId.PLAYER.id)
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
            oldItem!!.removeFromHand(player)
            item.addToHand(player)
            updateItemInHandForAll()
        }
    }

    var itemInHand: Item
        get() {
            val content = content[itemInHandSlot]
            return Objects.requireNonNullElseGet(content, Supplier { Item.Companion.create<Item>(ItemType.AIR) })
        }
        set(itemInHand) {
            this.setItem(itemInHandSlot, itemInHand)
            sendItemInHand()
        }

    fun getItemInHandSlot(): Int {
        return itemInHandSlot
    }

    fun setItemInHandSlot(itemInHandSlot: Int) {
        if (itemInHandSlot >= 0 && itemInHandSlot < 9) {
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
        mobEquipmentPacket.setRuntimeEntityId(entityHuman.entityId)
        mobEquipmentPacket.setItem(itemInHand.toItemData())
        mobEquipmentPacket.setContainerId(WindowId.PLAYER.id)
        mobEquipmentPacket.setHotbarSlot(itemInHandSlot)
        mobEquipmentPacket.setInventorySlot(itemInHandSlot)
        return mobEquipmentPacket
    }

    fun sendItemInHand() {
        if (holder is Player) {
            player.getPlayerConnection().sendPacket(createMobEquipmentPacket(player))
            this.sendContents(itemInHandSlot, player)
        }
    }

    fun updateItemInHandForAll() {
        if (holder is EntityHuman) {
            val mobEquipmentPacket: MobEquipmentPacket = createMobEquipmentPacket(entityHuman)
            for (onlinePlayers in entityHuman.getWorld().getPlayers()) {
                if (onlinePlayers !== entityHuman) {
                    onlinePlayers.playerConnection.sendPacket(mobEquipmentPacket)
                }
            }
        }
    }
}