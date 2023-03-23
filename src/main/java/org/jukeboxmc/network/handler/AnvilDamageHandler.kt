package org.jukeboxmc.network.handler

import org.cloudburstmc.protocol.bedrock.packet.AnvilDamagePacket
import org.jukeboxmc.Server
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class AnvilDamageHandler : PacketHandler<AnvilDamagePacket> {
    override fun handle(packet: AnvilDamagePacket, server: Server, player: Player) {}
}
