package org.jukeboxmc.network.handler

import org.jukeboxmc.Server
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class CraftingEventHandler : PacketHandler<CraftingEventPacket?> {
    override fun handle(packet: CraftingEventPacket?, server: Server, player: Player) {}
}