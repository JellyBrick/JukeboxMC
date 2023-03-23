package org.jukeboxmc.network.handler

import org.cloudburstmc.protocol.bedrock.packet.RespawnPacket
import org.jukeboxmc.Server
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class RespawnHandler : PacketHandler<RespawnPacket> {
    override fun handle(packet: RespawnPacket, server: Server, player: Player) {
        if (packet.state == RespawnPacket.State.CLIENT_READY) {
            val respawnPositionPacket = RespawnPacket()
            respawnPositionPacket.runtimeEntityId = player.entityId
            respawnPositionPacket.state = RespawnPacket.State.SERVER_READY
            respawnPositionPacket.position = player.spawnLocation.toVector3f()
            player.playerConnection.sendPacket(respawnPositionPacket)
        }
    }
}
