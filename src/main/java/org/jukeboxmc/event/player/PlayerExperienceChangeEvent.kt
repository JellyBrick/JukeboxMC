package org.jukeboxmc.event.player

import org.jukeboxmc.event.Cancellable
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
    newLevel: Int,
) : PlayerEvent(player), Cancellable {
    var newLevel: Int = newExperience
}
