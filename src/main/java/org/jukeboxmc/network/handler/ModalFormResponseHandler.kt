package org.jukeboxmc.network.handler

import org.cloudburstmc.protocol.bedrock.packet.ModalFormResponsePacket
import org.jukeboxmc.Server
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ModalFormResponseHandler : PacketHandler<ModalFormResponsePacket> {
    override fun handle(packet: ModalFormResponsePacket, server: Server, player: Player) {
        player.parseGUIResponse(packet.formId, packet.formData)
    }
}
