package org.jukeboxmc.network.handler

import org.jukeboxmc.Server
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ContainerCloseHandler : PacketHandler<ContainerClosePacket> {
    override fun handle(packet: ContainerClosePacket, server: Server, player: Player) {
        player.closingWindowId = packet.getId().toInt()
        player.closeInventory(packet.getId(), packet.isUnknownBool0())
        player.closingWindowId = Int.MIN_VALUE
    }
}