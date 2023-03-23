package org.jukeboxmc.network.handler

import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
import org.jukeboxmc.Server
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
interface PacketHandler<T : BedrockPacket> {
    fun handle(packet: T, server: Server, player: Player)
}
