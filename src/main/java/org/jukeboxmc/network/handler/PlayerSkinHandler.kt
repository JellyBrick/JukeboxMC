package org.jukeboxmc.network.handler

import org.jukeboxmc.Server
import org.jukeboxmc.player.Player
import org.jukeboxmc.player.skin.Skin

/**
 * @author LucGamesYT
 * @version 1.0
 */
class PlayerSkinHandler : PacketHandler<PlayerSkinPacket> {
    override fun handle(packet: PlayerSkinPacket, server: Server, player: Player) {
        player.skin = Skin.Companion.fromNetwork(packet.getSkin())
    }
}