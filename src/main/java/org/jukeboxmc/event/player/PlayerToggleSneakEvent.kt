package org.jukeboxmc.event.player

import org.jukeboxmc.event.Cancellable
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class PlayerToggleSneakEvent
/**
 * Creates a new [PlayerToggleSneakEvent]
 *
 * @param player     who toggled sneaking
 * @param isSneaking whether the player is sneaking
 */(
    player: Player,
    /**
     * Retrieves whether the player is sneaking or not
     *
     * @return whether the player is sneaking
     */
    val isSneaking: Boolean,
) : PlayerEvent(player), Cancellable
