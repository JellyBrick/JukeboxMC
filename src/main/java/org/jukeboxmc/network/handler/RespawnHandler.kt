package org.jukeboxmc.network.handler

import org.jukeboxmc.Server
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class RespawnHandler : PacketHandler<RespawnPacket> {
    override fun handle(packet: RespawnPacket, server: Server, player: Player) {
        if (packet.getState() == RespawnPacket.State.CLIENT_READY) {
            val respawnPositionPacket = RespawnPacket()
            respawnPositionPacket.setRuntimeEntityId(player.entityId)
            respawnPositionPacket.setState(RespawnPacket.State.SERVER_READY)
            respawnPositionPacket.setPosition(player.spawnLocation.toVector3f())
            player.playerConnection.sendPacket(respawnPositionPacket)
        }
    }
}