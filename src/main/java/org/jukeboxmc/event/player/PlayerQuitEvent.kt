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
    var quitMessage: String,
) : PlayerEvent(player)
