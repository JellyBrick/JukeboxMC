package org.jukeboxmc.event.player

import org.jukeboxmc.event.Cancellable
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class PlayerLoginEvent
/**
 * Creates a new [PlayerEvent]
 *
 * @param player who represents the player which comes with this event
 */
    (player: Player) : PlayerEvent(player), Cancellable {
    var kickReason = "Disconnected"
    private var canJoinFullServer = false
    fun canJoinFullServer(): Boolean {
        return canJoinFullServer
    }

    fun setCanJoinFullServer(canJoinFullServer: Boolean) {
        this.canJoinFullServer = canJoinFullServer
    }
}