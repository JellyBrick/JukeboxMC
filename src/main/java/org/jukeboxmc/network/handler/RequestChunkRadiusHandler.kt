package org.jukeboxmc.network.handler

import com.nukkitx.protocol.bedrock.packet.RequestChunkRadiusPacket
import org.jukeboxmc.Server
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class RequestChunkRadiusHandler : PacketHandler<RequestChunkRadiusPacket> {
    override fun handle(packet: RequestChunkRadiusPacket, server: Server, player: Player) {
        player.chunkRadius = packet.radius
    }
}
