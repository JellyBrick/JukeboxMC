package org.jukeboxmc.network.handler

import org.cloudburstmc.protocol.bedrock.data.PacketCompressionAlgorithm
import org.cloudburstmc.protocol.bedrock.packet.NetworkSettingsPacket
import org.cloudburstmc.protocol.bedrock.packet.PlayStatusPacket
import org.cloudburstmc.protocol.bedrock.packet.RequestNetworkSettingsPacket
import org.jukeboxmc.Server
import org.jukeboxmc.network.Network
import org.jukeboxmc.player.Player

/**
 * @author Kaooot
 * @version 1.0
 */
class RequestNetworkSettingsHandler : PacketHandler<RequestNetworkSettingsPacket> {
    override fun handle(packet: RequestNetworkSettingsPacket, server: Server, player: Player) {
        if (player.playerConnection.isLoggedIn()) {
            player.playerConnection.disconnect("Player is already logged in.")
            return
        }
        val protocolVersion: Int = packet.protocolVersion
        val currentProtocol: Int = Network.CODEC.protocolVersion
        if (protocolVersion != currentProtocol) {
            player.playerConnection.sendPlayStatus(if (protocolVersion > currentProtocol) PlayStatusPacket.Status.LOGIN_FAILED_SERVER_OLD else PlayStatusPacket.Status.LOGIN_FAILED_CLIENT_OLD)
            return
        }
        val compressionAlgorithm: PacketCompressionAlgorithm = server.compressionAlgorithm
        val networkSettingsPacket = NetworkSettingsPacket()
        networkSettingsPacket.compressionThreshold = 0
        networkSettingsPacket.compressionAlgorithm = compressionAlgorithm
        player.playerConnection.sendPacketImmediately(networkSettingsPacket)
        player.playerConnection.getSession().setCompression(compressionAlgorithm)
    }
}
