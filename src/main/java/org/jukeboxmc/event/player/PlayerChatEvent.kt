package org.jukeboxmc.event.player

import org.jukeboxmc.event.Cancellable
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class PlayerChatEvent
/**
 * Creates a new [PlayerEvent]
 *
 * @param player who represents the player which comes with this event
 */(player: Player, var format: String, var message: String) : PlayerEvent(player), Cancellable