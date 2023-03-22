package org.jukeboxmc.event.inventory

import org.jukeboxmc.event.Event
import org.jukeboxmc.inventory.Inventory

/**
 * @author LucGamesYT
 * @version 1.0
 */
abstract class InventoryEvent
/**
 * Creates a new [InventoryEvent]
 *
 * @param inventory the representation of the event's affected [Inventory]
 */(
    /**
     * Retrieves the affected [Inventory] which comes with this [InventoryEvent]
     *
     * @return a fresh [Inventory]
     */
    val inventory: Inventory,
) : Event()
