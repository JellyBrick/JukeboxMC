package org.jukeboxmc.inventory

import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class CreativeItemCacheInventory(holder: InventoryHolder) : Inventory(holder, 1) {
    override fun sendContents(player: Player) {}
    override fun sendContents(slot: Int, player: Player) {}
    override val inventoryHolder: InventoryHolder?
        get() = holder as Player
    override val type: InventoryType
        get() = InventoryType.CREATIVE
}