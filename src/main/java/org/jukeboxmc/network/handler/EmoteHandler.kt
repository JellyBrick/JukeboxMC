package org.jukeboxmc.network.handler

import org.jukeboxmc.Server
import org.jukeboxmc.player.Player

/**
 * @author pooooooon
 * @version 1.0
 */
class EmoteHandler : PacketHandler<EmotePacket> {
    override fun handle(packet: EmotePacket, server: Server, player: Player) {
        if (packet.getRuntimeEntityId() != player.entityId) {
            return
        }
        packet.getFlags().add(EmoteFlag.SERVER_SIDE)
        server.broadcastPacket(packet)
    }
}