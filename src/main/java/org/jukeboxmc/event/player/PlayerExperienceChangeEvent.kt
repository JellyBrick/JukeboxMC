package org.jukeboxmc.event.player

import org.jukeboxmc.event.Cancellable
import org.jukeboxmc.event.player.PlayerEvent
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class PlayerExperienceChangeEvent(
    player: Player,
    val oldExperience: Int,
    val oldLevel: Int,
    var newExperience: Int,
    newLevel: Int
) : PlayerEvent(player), Cancellable {
    var newLevel: Int

    /**
     * Creates a new [PlayerEvent]
     *
     * @param player who represents the player which comes with this event
     */
    init {
        this.newLevel = newExperience
    }
}