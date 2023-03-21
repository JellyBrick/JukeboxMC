package org.jukeboxmc.event.player

import org.jukeboxmc.event.Cancellable
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class PlayerToggleGlideEvent
/**
 * Creates a new [PlayerToggleGlideEvent]
 *
 * @param player    who toggled gliding
 * @param isGliding whether the player is gliding
 */(
    player: Player,
    /**
     * Retreives whether the player is gliding or not
     *
     * @return whether the player is gliding
     */
    val isGliding: Boolean
) : PlayerEvent(player), Cancellable