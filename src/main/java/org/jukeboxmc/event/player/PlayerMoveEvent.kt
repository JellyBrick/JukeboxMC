package org.jukeboxmc.event.player

import org.jukeboxmc.event.Cancellable
import org.jukeboxmc.math.Location
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class PlayerMoveEvent
/**
 * Creates a new [PlayerMoveEvent]
 *
 * @param player who moved
 * @param from   which represents the position the player stood at
 * @param to     which represents the players currently position
 */(
    player: Player,
    var from: Location,
    var to: Location,
) : PlayerEvent(player), Cancellable
