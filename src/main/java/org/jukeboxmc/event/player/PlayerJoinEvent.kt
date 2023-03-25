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
 * @param joinMessage which will be sent when the player joins
 */(
    player: Player,
    var joinMessage: String,
) : PlayerEvent(player)
