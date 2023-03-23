package org.jukeboxmc.network.handler

import org.cloudburstmc.protocol.bedrock.data.entity.EntityEventType
import org.cloudburstmc.protocol.bedrock.packet.EntityEventPacket
import org.jukeboxmc.Server
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class EntityEventHandler : PacketHandler<EntityEventPacket> {
    override fun handle(packet: EntityEventPacket, server: Server, player: Player) {
        if (packet.type == EntityEventType.EATING_ITEM) {
            if (packet.data == 0 || packet.runtimeEntityId != player.entityId) {
                return
            }
            server.broadcastPacket(packet)
        }
    }
}
