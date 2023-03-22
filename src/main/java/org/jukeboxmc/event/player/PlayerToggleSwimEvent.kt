package org.jukeboxmc.event.player

import org.jukeboxmc.event.Cancellable
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class PlayerToggleSwimEvent
/**
 * Creates a new [PlayerToggleSwimEvent]
 *
 * @param player     who toggled swimming
 * @param isSwimming whether the player is swimming
 */(
    player: Player,
    /**
     * Retrieves whether the player is swimming or not
     *
     * @return whether the player is swimming
     */
    val isSwimming: Boolean,
) : PlayerEvent(player), Cancellable
