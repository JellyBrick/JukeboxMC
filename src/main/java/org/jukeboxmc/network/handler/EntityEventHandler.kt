package org.jukeboxmc.network.handler

import org.jukeboxmc.Server
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class EntityEventHandler : PacketHandler<EntityEventPacket> {
    override fun handle(packet: EntityEventPacket, server: Server, player: Player) {
        if (packet.getType() == EntityEventType.EATING_ITEM) {
            if (packet.getData() == 0 || packet.getRuntimeEntityId() != player.entityId) {
                return
            }
            server.broadcastPacket(packet)
        }
    }
}