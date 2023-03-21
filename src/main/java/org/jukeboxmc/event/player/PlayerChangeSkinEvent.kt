package org.jukeboxmc.event.player

import org.jukeboxmc.event.Cancellable
import org.jukeboxmc.player.Player
import org.jukeboxmc.player.skin.Skin

/**
 * @author LucGamesYT
 * @version 1.0
 */
class PlayerChangeSkinEvent
/**
 * Creates a new [PlayerEvent]
 *
 * @param player who represents the player which comes with this event
 */(player: Player, var skin: Skin) : PlayerEvent(player), Cancellable