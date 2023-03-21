package org.jukeboxmc.network.handler

import org.jukeboxmc.Server
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class SetLocalPlayerAsInitializedHandler : PacketHandler<SetLocalPlayerAsInitializedPacket?> {
    override fun handle(packet: SetLocalPlayerAsInitializedPacket?, server: Server, player: Player) {
        val playerJoinEvent = PlayerJoinEvent(player, "§e" + player.name + " has joined the game")
        Server.Companion.getInstance().getPluginManager().callEvent(playerJoinEvent)
        if (playerJoinEvent.getJoinMessage() != null && !playerJoinEvent.getJoinMessage().isEmpty()) {
            Server.Companion.getInstance().broadcastMessage(playerJoinEvent.getJoinMessage())
        }
    }
}