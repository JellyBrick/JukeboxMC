package org.jukeboxmc.network.handler

import com.nukkitx.protocol.bedrock.packet.AnimatePacket
import org.jukeboxmc.Server
import org.jukeboxmc.player.Player
import java.util.stream.Collectors

/**
 * @author LucGamesYT
 * @version 1.0
 */
class AnimateHandler : PacketHandler<AnimatePacket> {
    override fun handle(packet: AnimatePacket, server: Server, player: Player) {
        if (packet.action == AnimatePacket.Action.SWING_ARM) {
            val players =
                player.server.onlinePlayers.stream().filter { p: Player? -> p != player }.collect(Collectors.toSet())
            if (players.isNotEmpty()) {
                player.server.broadcastPacket(players, packet)
            }
        }
    }
}
