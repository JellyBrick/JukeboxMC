package org.jukeboxmc.event.inventory

import org.jukeboxmc.inventory.Inventory
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class InventoryCloseEvent
/**
 * Creates a new [InventoryCloseEvent]
 *
 * @param inventory which was closed
 * @param player    who closed the [Inventory]
 */(
    inventory: Inventory,
    /**
     * Retrieves the [Player] who caused this [InventoryCloseEvent]
     *
     * @return a fresh [Player]
     */
    val player: Player
) : InventoryEvent(inventory)