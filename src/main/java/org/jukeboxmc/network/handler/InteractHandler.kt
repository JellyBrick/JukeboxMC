package org.jukeboxmc.network.handler

import org.jukeboxmc.Server
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class InteractHandler : PacketHandler<InteractPacket> {
    override fun handle(packet: InteractPacket, server: Server, player: Player) {
        if (packet.getAction() == InteractPacket.Action.OPEN_INVENTORY) {
            player.openInventory(player.inventory)
        }
    }
}