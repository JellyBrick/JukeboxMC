package org.jukeboxmc.network.handler

import org.jukeboxmc.Server
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class TextHandler : PacketHandler<TextPacket> {
    override fun handle(packet: TextPacket, server: Server, player: Player) {
        if (packet.getType() == TextPacket.Type.CHAT) {
            val playerChatEvent = PlayerChatEvent(player, "<" + player.name + "> ", packet.getMessage())
            server.pluginManager.callEvent(playerChatEvent)
            if (playerChatEvent.isCancelled()) {
                return
            }
            for (onlinePlayer in player.server.onlinePlayers) {
                onlinePlayer!!.sendMessage(playerChatEvent.getFormat() + playerChatEvent.getMessage())
            }
            server.logger.info(playerChatEvent.getFormat() + playerChatEvent.getMessage())
        }
    }
}