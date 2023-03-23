package org.jukeboxmc.network.handler

import org.cloudburstmc.protocol.bedrock.packet.SetLocalPlayerAsInitializedPacket
import org.jukeboxmc.Server
import org.jukeboxmc.event.player.PlayerJoinEvent
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class SetLocalPlayerAsInitializedHandler : PacketHandler<SetLocalPlayerAsInitializedPacket> {
    override fun handle(packet: SetLocalPlayerAsInitializedPacket, server: Server, player: Player) {
        val playerJoinEvent = PlayerJoinEvent(player, "§e" + player.name + " has joined the game")
        Server.instance.pluginManager.callEvent(playerJoinEvent)
        if (playerJoinEvent.joinMessage.isNotEmpty()) {
            Server.instance.broadcastMessage(playerJoinEvent.joinMessage)
        }
    }
}
