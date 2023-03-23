package org.jukeboxmc.inventory

import org.cloudburstmc.protocol.bedrock.packet.InventoryContentPacket
import org.cloudburstmc.protocol.bedrock.packet.InventorySlotPacket
import org.cloudburstmc.protocol.bedrock.packet.MobArmorEquipmentPacket
import org.jukeboxmc.Server
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemArmor
import org.jukeboxmc.player.Player
import org.jukeboxmc.world.Sound

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ArmorInventory(holder: InventoryHolder) : ContainerInventory(holder, 4) {
    override val inventoryHolder: Player
        get() = holder as Player
    override val type: InventoryType
        get() = InventoryType.ARMOR

    override fun sendContents(player: Player) {
        if (inventoryHolder == player) {
            val inventoryContentPacket = InventoryContentPacket()
            inventoryContentPacket.containerId = WindowId.ARMOR_DEPRECATED.id
            inventoryContentPacket.contents = this.itemDataContents
            player.playerConnection.sendPacket(inventoryContentPacket)
        } else {
            sendMobArmor(player)
        }
    }

    override fun sendContents(slot: Int, player: Player) {
        if (inventoryHolder == player) {
            val inventorySlotPacket = InventorySlotPacket()
            inventorySlotPacket.containerId = WindowId.ARMOR_DEPRECATED.id
            inventorySlotPacket.slot = slot
            inventorySlotPacket.item = contents[slot].toItemData()
            player.playerConnection.sendPacket(inventorySlotPacket)
        } else {
            sendMobArmor(player)
        }
    }

    override fun setItem(slot: Int, item: Item, sendContent: Boolean) {
        super.setItem(slot, item, sendContent)
        if (holder is Player) {
            val player = holder as Player
            val armorInventory: ArmorInventory = player.armorInventory
            val mobArmorEquipmentPacket = MobArmorEquipmentPacket()
            mobArmorEquipmentPacket.runtimeEntityId = holderId
            mobArmorEquipmentPacket.helmet = armorInventory.helmet.toItemData()
            mobArmorEquipmentPacket.chestplate = armorInventory.chestplate.toItemData()
            mobArmorEquipmentPacket.leggings = armorInventory.leggings.toItemData()
            mobArmorEquipmentPacket.boots = armorInventory.boots.toItemData()
            for (onlinePlayer in Server.instance.onlinePlayers) {
                if (onlinePlayer == player) {
                    val inventorySlotPacket = InventorySlotPacket()
                    inventorySlotPacket.containerId = WindowId.ARMOR_DEPRECATED.id
                    inventorySlotPacket.slot = slot
                    inventorySlotPacket.item = contents[slot].toItemData()
                    onlinePlayer.playerConnection.sendPacket(inventorySlotPacket)
                    onlinePlayer.playerConnection.sendPacket(mobArmorEquipmentPacket)
                } else {
                    onlinePlayer.playerConnection.sendPacket(mobArmorEquipmentPacket)
                }
            }
        }
    }

    private fun sendMobArmor(player: Player) {
        val mobArmorEquipmentPacket = MobArmorEquipmentPacket()
        mobArmorEquipmentPacket.runtimeEntityId = inventoryHolder.entityId
        mobArmorEquipmentPacket.helmet = contents[0].toItemData()
        mobArmorEquipmentPacket.chestplate = contents[1].toItemData()
        mobArmorEquipmentPacket.leggings = contents[2].toItemData()
        mobArmorEquipmentPacket.boots = contents[3].toItemData()
        player.playerConnection.sendPacket(mobArmorEquipmentPacket)
    }

    var helmet: Item
        get() = contents[0].clone()
        set(item) {
            this.setItem(0, item)
        }
    var chestplate: Item
        get() = contents[1].clone()
        set(item) {
            this.setItem(1, item)
        }
    var leggings: Item
        get() = contents[2].clone()
        set(item) {
            this.setItem(2, item)
        }
    var boots: Item
        get() = contents[3].clone()
        set(item) {
            this.setItem(3, item)
        }
    val totalArmorValue: Float
        get() {
            var armorValue = 0f
            for (itemStack in contents) {
                if (itemStack is ItemArmor) {
                    armorValue += itemStack.armorPoints.toFloat()
                }
            }
            return armorValue
        }

    fun damageEvenly(damage: Float) {
        var damage = damage
        damage /= 4.0f
        if (damage < 1.0f) {
            damage = 1.0f
        }
        if (holder is Player) {
            val player = holder as Player
            val helmet = helmet
            if (helmet.type != ItemType.AIR) {
                if (helmet.calculateDurability(damage.toInt())) {
                    this.helmet = Item.create(ItemType.AIR)
                    player.playSound(Sound.RANDOM_BREAK, 1f, 1f)
                } else {
                    this.helmet = helmet
                }
            }
            val chestplate = chestplate
            if (chestplate.type != ItemType.AIR) {
                if (chestplate.calculateDurability(damage.toInt())) {
                    this.chestplate = Item.create(ItemType.AIR)
                    player.playSound(Sound.RANDOM_BREAK, 1f, 1f)
                } else {
                    this.chestplate = chestplate
                }
            }
            val leggings = leggings
            if (leggings.type != ItemType.AIR) {
                if (leggings.calculateDurability(damage.toInt())) {
                    this.leggings = Item.create(ItemType.AIR)
                    player.playSound(Sound.RANDOM_BREAK, 1f, 1f)
                } else {
                    this.leggings = leggings
                }
            }
            val boots = boots
            if (boots.type != ItemType.AIR) {
                if (boots.calculateDurability(damage.toInt())) {
                    this.boots = Item.create(ItemType.AIR)
                    player.playSound(Sound.RANDOM_BREAK, 1f, 1f)
                } else {
                    this.boots = boots
                }
            }
        }
    }
}
