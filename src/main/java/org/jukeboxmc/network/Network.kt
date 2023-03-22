package org.jukeboxmc.network

import com.nukkitx.protocol.bedrock.BedrockPacketCodec
import com.nukkitx.protocol.bedrock.BedrockPong
import com.nukkitx.protocol.bedrock.BedrockServer
import com.nukkitx.protocol.bedrock.BedrockServerEventHandler
import com.nukkitx.protocol.bedrock.BedrockServerSession
import com.nukkitx.protocol.bedrock.v575.Bedrock_v575
import org.jukeboxmc.Server
import org.jukeboxmc.player.PlayerConnection
import java.net.InetSocketAddress
import java.util.function.Consumer

/**
 * @author LucGamesYT
 * @version 1.0
 */
class Network(val server: Server, inetSocketAddress: InetSocketAddress) : BedrockServerEventHandler {
    private val inetSocketAddress: InetSocketAddress
    private val bedrockPong: BedrockPong
    private val bedrockServer: BedrockServer
    private val connections: MutableSet<PlayerConnection> = HashSet()
    private val updater: Consumer<PlayerConnection>

    init {
        this.inetSocketAddress = inetSocketAddress
        bedrockPong = BedrockPong()
        bedrockServer = BedrockServer(inetSocketAddress)
        bedrockServer.handler = this
        updater = Consumer<PlayerConnection> { obj: PlayerConnection -> obj.update() }
        try {
            bedrockServer.bind().join()
            server.logger.info("Server started successfully at " + this.inetSocketAddress.hostString + ":" + this.inetSocketAddress.port + "!")
        } catch (e: Exception) {
            server.logger.error("Could not start server! Is there already running something on this port?", e)
        }
    }

    override fun onQuery(inetSocketAddress: InetSocketAddress): BedrockPong {
        bedrockPong.edition = "MCPE"
        bedrockPong.gameType = server.gameMode.identifier
        bedrockPong.motd = server.motd
        bedrockPong.subMotd = server.subMotd
        bedrockPong.playerCount = server.onlinePlayers.size
        bedrockPong.maximumPlayerCount = server.maxPlayers
        bedrockPong.ipv4Port = this.inetSocketAddress.port
        bedrockPong.isNintendoLimited = false
        bedrockPong.protocolVersion = CODEC.protocolVersion
        bedrockPong.version = CODEC.minecraftVersion
        return bedrockPong
    }

    override fun onConnectionRequest(address: InetSocketAddress): Boolean {
        return server.getFinishedState().get() && server.getRunningState().get()
    }

    override fun onSessionCreation(bedrockServerSession: BedrockServerSession) {
        server.addPlayer(addPlayer(PlayerConnection(server, bedrockServerSession)).player)
    }

    @Synchronized
    private fun addPlayer(playerConnection: PlayerConnection): PlayerConnection {
        connections.add(playerConnection)
        return playerConnection
    }

    @Synchronized
    fun update() {
        connections.removeIf { obj: PlayerConnection -> obj.isClosed }
        connections.forEach(updater)
    }

    fun getBedrockServer(): BedrockServer {
        return bedrockServer
    }

    companion object {
        val CODEC: BedrockPacketCodec = Bedrock_v575.V575_CODEC
    }
}
