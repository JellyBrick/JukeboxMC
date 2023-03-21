package org.jukeboxmc.event.inventory

import org.jukeboxmc.event.Cancellable
import org.jukeboxmc.inventory.Inventory
import org.jukeboxmc.item.Item
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class InventoryClickEvent
/**
 * Creates a new [InventoryClickEvent]
 *
 * @param inventory  which represents the inventory the [Player] clicked at
 * @param player     who clicked on a item in the [Inventory]
 * @param sourceItem which stands for the old item
 * @param targetItem which is the new item
 * @param slot       the slot where the item was placed at
 */(
    inventory: Inventory, val destinationInventory: Inventory,
    /**
     * Retrieves the [Player] who caused this [InventoryClickEvent]
     *
     * @return a fresh [Player]
     */
    val player: Player,
    /**
     * Retrieves the old [Item]
     *
     * @return a fresh [Item]
     */
    val sourceItem: Item,
    /**
     * Retrieves the new [Item]
     *
     * @return a fresh [Item]
     */
    val targetItem: Item,
    /**
     * Retrieves the slot
     *
     * @return a fresh int value
     */
    val slot: Int
) : InventoryEvent(inventory), Cancellable