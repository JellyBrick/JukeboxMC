package org.jukeboxmc.event.player

import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class PlayerJumpEvent
/**
 * Creates a new [PlayerEvent]
 *
 * @param player who represents the player which comes with this event
 */
    (player: Player) : PlayerEvent(player)