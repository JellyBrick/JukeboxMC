package org.jukeboxmc.network.handler

import com.nukkitx.protocol.bedrock.packet.TextPacket
import org.jukeboxmc.Server
import org.jukeboxmc.event.player.PlayerChatEvent
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class TextHandler : PacketHandler<TextPacket> {
    override fun handle(packet: TextPacket, server: Server, player: Player) {
        if (packet.type == TextPacket.Type.CHAT) {
            val playerChatEvent = PlayerChatEvent(player, "<" + player.name + "> ", packet.message)
            server.pluginManager.callEvent(playerChatEvent)
            if (playerChatEvent.isCancelled) {
                return
            }
            for (onlinePlayer in player.server.onlinePlayers) {
                onlinePlayer.sendMessage(playerChatEvent.format + playerChatEvent.message)
            }
            server.logger.info(playerChatEvent.format + playerChatEvent.message)
        }
    }
}
