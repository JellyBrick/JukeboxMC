package org.jukeboxmc.network.handler

import java.util.stream.Collectors
import org.jukeboxmc.Server
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class AnimateHandler : PacketHandler<AnimatePacket> {
    override fun handle(packet: AnimatePacket, server: Server, player: Player) {
        if (packet.getAction() == AnimatePacket.Action.SWING_ARM) {
            val players =
                player.server.onlinePlayers.stream().filter { p: Player? -> p != player }.collect(Collectors.toSet())
            if (!players.isEmpty()) {
                player.server.broadcastPacket(players, packet)
            }
        }
    }
}