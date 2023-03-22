package org.jukeboxmc.event.player

import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class PlayerQuitEvent
/**
 * Creates a new [PlayerQuitEvent]
 *
 * @param player      who has left the server
 * @param quitMessage which will be sent when the player leaves the server
 */(
    player: Player,
    /**
     * Modifies the leave message
     *
     * @param quitMessage which should be modified
     */
    var quitMessage: String,
) : PlayerEvent(player) {
    /**
     * Retrieves the leave message
     *
     * @return a fresh [String]
     */
}
