package org.jukeboxmc.event.player

import org.jukeboxmc.math.Location
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class PlayerRespawnEvent
/**
 * Creates a new [PlayerEvent]
 *
 * @param player who represents the player which comes with this event
 */(player: Player, var respawnLocation: Location) : PlayerEvent(player)