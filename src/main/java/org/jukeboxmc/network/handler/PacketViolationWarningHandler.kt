package org.jukeboxmc.network.handler

import org.jukeboxmc.Server
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class PacketViolationWarningHandler : PacketHandler<PacketViolationWarningPacket> {
    override fun handle(packet: PacketViolationWarningPacket, server: Server, player: Player) {
        server.logger.info(packet.toString())
    }
}