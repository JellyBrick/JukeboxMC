package org.jukeboxmc.event.player

import org.jukeboxmc.event.Cancellable
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class PlayerToggleSprintEvent
/**
 * Creates a new [PlayerToggleSprintEvent]
 *
 * @param player      who toggled sprinting
 * @param isSprinting whether the player is sprinting
 */(
    player: Player,
    /**
     * Retrieves whether the player is sprinting or not
     *
     * @return whether the player is sprinting
     */
    val isSprinting: Boolean
) : PlayerEvent(player), Cancellable