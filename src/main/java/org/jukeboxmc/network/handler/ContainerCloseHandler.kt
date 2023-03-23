package org.jukeboxmc.network.handler

import org.cloudburstmc.protocol.bedrock.packet.ContainerClosePacket
import org.jukeboxmc.Server
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ContainerCloseHandler : PacketHandler<ContainerClosePacket> {
    override fun handle(packet: ContainerClosePacket, server: Server, player: Player) {
        player.closingWindowId = packet.id.toInt()
        player.closeInventory(packet.id, packet.isServerInitiated)
        player.closingWindowId = Int.MIN_VALUE
    }
}
