package org.jukeboxmc.event.inventory

import org.jukeboxmc.event.Cancellable
import org.jukeboxmc.inventory.Inventory
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class InventoryOpenEvent
/**
 * Creates a new [InventoryOpenEvent]
 *
 * @param inventory which was opened
 * @param player    who opened the [Inventory]
 */(
    inventory: Inventory,
    /**
     * Retrieves the [Player] who caused this [InventoryOpenEvent]
     *
     * @return a fresh [Player]
     */
    val player: Player
) : InventoryEvent(inventory), Cancellable