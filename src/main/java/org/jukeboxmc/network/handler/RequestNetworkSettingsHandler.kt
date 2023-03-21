package org.jukeboxmc.network.handler

import org.jukeboxmc.Server
import org.jukeboxmc.network.Network
import org.jukeboxmc.player.Player

/**
 * @author Kaooot
 * @version 1.0
 */
class RequestNetworkSettingsHandler : PacketHandler<RequestNetworkSettingsPacket> {
    override fun handle(packet: RequestNetworkSettingsPacket, server: Server, player: Player) {
        if (player.playerConnection.isLoggedIn) {
            player.playerConnection.disconnect("Player is already logged in.")
            return
        }
        val protocolVersion: Int = packet.getProtocolVersion()
        val currentProtocol: Int = Network.Companion.CODEC.getProtocolVersion()
        if (protocolVersion != currentProtocol) {
            player.playerConnection.sendPlayStatus(if (protocolVersion > currentProtocol) PlayStatusPacket.Status.LOGIN_FAILED_SERVER_OLD else PlayStatusPacket.Status.LOGIN_FAILED_CLIENT_OLD)
            return
        }
        val compressionAlgorithm: PacketCompressionAlgorithm? = server.compressionAlgorithm
        val networkSettingsPacket = NetworkSettingsPacket()
        networkSettingsPacket.setCompressionThreshold(0)
        networkSettingsPacket.setCompressionAlgorithm(compressionAlgorithm)
        player.playerConnection.sendPacketImmediately(networkSettingsPacket)
        player.playerConnection.session.setCompression(compressionAlgorithm)
    }
}