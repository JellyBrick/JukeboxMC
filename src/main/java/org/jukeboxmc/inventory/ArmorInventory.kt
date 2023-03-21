package org.jukeboxmc.inventory

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
    override val inventoryHolder: InventoryHolder?
        get() = holder as Player
    override val type: InventoryType
        get() = InventoryType.ARMOR

    override fun sendContents(player: Player) {
        if (inventoryHolder == player) {
            val inventoryContentPacket = InventoryContentPacket()
            inventoryContentPacket.setContainerId(WindowId.ARMOR_DEPRECATED.id)
            inventoryContentPacket.setContents(this.itemDataContents)
            player.playerConnection.sendPacket(inventoryContentPacket)
        } else {
            sendMobArmor(player)
        }
    }

    override fun sendContents(slot: Int, player: Player) {
        if (inventoryHolder == player) {
            val inventorySlotPacket = InventorySlotPacket()
            inventorySlotPacket.setContainerId(WindowId.ARMOR_DEPRECATED.id)
            inventorySlotPacket.setSlot(slot)
            inventorySlotPacket.setItem(content[slot].toItemData())
            player.playerConnection.sendPacket(inventorySlotPacket)
        } else {
            sendMobArmor(player)
        }
    }

    override fun setItem(slot: Int, item: Item, sendContent: Boolean) {
        super.setItem(slot, item, sendContent)
        if (holder is Player) {
            val armorInventory: ArmorInventory = player.getArmorInventory()
            val mobArmorEquipmentPacket = MobArmorEquipmentPacket()
            mobArmorEquipmentPacket.setRuntimeEntityId(holderId)
            mobArmorEquipmentPacket.setHelmet(armorInventory.helmet.toItemData())
            mobArmorEquipmentPacket.setChestplate(armorInventory.chestplate.toItemData())
            mobArmorEquipmentPacket.setLeggings(armorInventory.leggings.toItemData())
            mobArmorEquipmentPacket.setBoots(armorInventory.boots.toItemData())
            for (onlinePlayer in Server.Companion.getInstance().getOnlinePlayers()) {
                if (onlinePlayer == player) {
                    val inventorySlotPacket = InventorySlotPacket()
                    inventorySlotPacket.setContainerId(WindowId.ARMOR_DEPRECATED.id)
                    inventorySlotPacket.setSlot(slot)
                    inventorySlotPacket.setItem(content[slot].toItemData())
                    onlinePlayer.getPlayerConnection().sendPacket(inventorySlotPacket)
                    onlinePlayer.getPlayerConnection().sendPacket(mobArmorEquipmentPacket)
                } else {
                    onlinePlayer.getPlayerConnection().sendPacket(mobArmorEquipmentPacket)
                }
            }
        }
    }

    private fun sendMobArmor(player: Player) {
        val mobArmorEquipmentPacket = MobArmorEquipmentPacket()
        mobArmorEquipmentPacket.setRuntimeEntityId(inventoryHolder.getEntityId())
        mobArmorEquipmentPacket.setHelmet(content[0].toItemData())
        mobArmorEquipmentPacket.setChestplate(content[1].toItemData())
        mobArmorEquipmentPacket.setLeggings(content[2].toItemData())
        mobArmorEquipmentPacket.setBoots(content[3].toItemData())
        player.playerConnection.sendPacket(mobArmorEquipmentPacket)
    }

    var helmet: Item
        get() = content[0]
        set(item) {
            this.setItem(0, item)
        }
    var chestplate: Item
        get() = content[1]
        set(item) {
            this.setItem(1, item)
        }
    var leggings: Item
        get() = content[2]
        set(item) {
            this.setItem(2, item)
        }
    var boots: Item
        get() = content[3]
        set(item) {
            this.setItem(3, item)
        }
    val totalArmorValue: Float
        get() {
            var armorValue = 0f
            for (itemStack in content) {
                if (itemStack is ItemArmor) {
                    armorValue += itemStack.armorPoints.toFloat()
                }
            }
            return armorValue
        }

    fun damageEvenly(damage: Float) {
        var damage = damage
        damage = damage / 4.0f
        if (damage < 1.0f) {
            damage = 1.0f
        }
        if (holder is Player) {
            val helmet = helmet
            if (helmet != null && helmet.type != ItemType.AIR) {
                if (helmet.calculateDurability(damage.toInt())) {
                    this.helmet = Item.Companion.create<Item>(ItemType.AIR)
                    player.playSound(Sound.RANDOM_BREAK, 1f, 1f)
                } else {
                    this.helmet = helmet
                }
            }
            val chestplate = chestplate
            if (chestplate != null && chestplate.type != ItemType.AIR) {
                if (chestplate.calculateDurability(damage.toInt())) {
                    this.chestplate = Item.Companion.create<Item>(ItemType.AIR)
                    player.playSound(Sound.RANDOM_BREAK, 1f, 1f)
                } else {
                    this.chestplate = chestplate
                }
            }
            val leggings = leggings
            if (leggings != null && leggings.type != ItemType.AIR) {
                if (leggings.calculateDurability(damage.toInt())) {
                    this.leggings = Item.Companion.create<Item>(ItemType.AIR)
                    player.playSound(Sound.RANDOM_BREAK, 1f, 1f)
                } else {
                    this.leggings = leggings
                }
            }
            val boots = boots
            if (boots != null && boots.type != ItemType.AIR) {
                if (boots.calculateDurability(damage.toInt())) {
                    this.boots = Item.Companion.create<Item>(ItemType.AIR)
                    player.playSound(Sound.RANDOM_BREAK, 1f, 1f)
                } else {
                    this.boots = boots
                }
            }
        }
    }
}