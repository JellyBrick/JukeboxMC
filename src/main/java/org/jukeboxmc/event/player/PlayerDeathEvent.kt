package org.jukeboxmc.event.player

import org.jukeboxmc.item.Item
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class PlayerDeathEvent
/**
 * Creates a new [PlayerEvent]
 *
 * @param player who represents the player which comes with this event
 */(player: Player, var deathMessage: String, var isDropInventory: Boolean, var drops: List<Item>) :
    PlayerEvent(player) {
    var deathScreenMessage: String? = null

}