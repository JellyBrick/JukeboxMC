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
    /**
     * Modifies the from [Location]
     *
     * @param from which should be modified
     */
    var from: Location,
    /**
     * Modifies the to [Location]
     *
     * @param to which should be modifies
     */
    var to: Location,
) : PlayerEvent(player), Cancellable {
    /**
     * Retrieves the from [Location]
     *
     * @return a fresh [Location]
     */
    /**
     * Retrieves the to [Location]
     *
     * @return a fresh [Location]
     */
}
