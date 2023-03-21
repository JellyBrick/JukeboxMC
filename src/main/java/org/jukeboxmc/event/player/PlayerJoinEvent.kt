package org.jukeboxmc.event.player

import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class PlayerJoinEvent
/**
 * Creates a new [PlayerJoinEvent]
 *
 * @param player      who joined the server
 * @param joinMessage which will be send when the player joins
 */(
    player: Player,
    /**
     * Modifies the player join message
     *
     * @param joinMessage which should be modified
     */
    var joinMessage: String
) : PlayerEvent(player) {
    /**
     * Retrieves the player join message
     *
     * @return a fresh [String]
     */

}