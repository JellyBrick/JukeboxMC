package org.jukeboxmc.event.player

import org.jukeboxmc.event.Event
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
abstract class PlayerEvent
/**
 * Creates a new [PlayerEvent]
 *
 * @param player who represents the player which comes with this event
 */(
    /**
     * Retrives the [Player] which comes with this [PlayerEvent]
     *
     * @return a fresh [Player]
     */
    val player: Player
) : Event()