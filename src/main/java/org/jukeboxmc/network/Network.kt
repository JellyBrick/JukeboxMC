package org.jukeboxmc.network

import java.util.function.Consumer
import java.util.function.Predicate
import org.jukeboxmc.Server

/**
 * @author LucGamesYT
 * @version 1.0
 */
class Network(val server: Server, inetSocketAddress: InetSocketAddress) : BedrockServerEventHandler {
    private val inetSocketAddress: InetSocketAddress
    private val bedrockPong: BedrockPong
    private val bedrockServer: BedrockServer
    private val connections: MutableSet<PlayerConnection> = HashSet<PlayerConnection>()
    private val removePredicate: Predicate<PlayerConnection>
    private val updater: Consumer<PlayerConnection>

    init {
        this.inetSocketAddress = inetSocketAddress
        bedrockPong = BedrockPong()
        bedrockServer = BedrockServer(inetSocketAddress)
        bedrockServer.setHandler(this)
        removePredicate = Predicate<PlayerConnection> { obj: PlayerConnection -> obj.isClosed() }
        updater = Consumer<PlayerConnection> { obj: PlayerConnection -> obj.update() }
        try {
            bedrockServer.bind().join()
            server.logger.info("Server started successfully at " + this.inetSocketAddress.getHostString() + ":" + this.inetSocketAddress.getPort() + "!")
        } catch (e: Exception) {
            server.logger.error("Could not start server! Is there already running something on this port?", e)
        }
    }

    override fun onQuery(inetSocketAddress: InetSocketAddress): BedrockPong? {
        bedrockPong.setEdition("MCPE")
        bedrockPong.setGameType(server.gameMode.identifier)
        bedrockPong.setMotd(server.motd)
        bedrockPong.setSubMotd(server.subMotd)
        bedrockPong.setPlayerCount(server.onlinePlayers.size)
        bedrockPong.setMaximumPlayerCount(server.maxPlayers)
        bedrockPong.setIpv4Port(this.inetSocketAddress.getPort())
        bedrockPong.setNintendoLimited(false)
        bedrockPong.setProtocolVersion(CODEC.getProtocolVersion())
        bedrockPong.setVersion(CODEC.getMinecraftVersion())
        return bedrockPong
    }

    override fun onConnectionRequest(address: InetSocketAddress): Boolean {
        return server.finishedState.get() && server.runningState.get()
    }

    override fun onSessionCreation(bedrockServerSession: BedrockServerSession) {
        try {
            server.addPlayer(addPlayer(PlayerConnection(server, bedrockServerSession)).getPlayer())
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    @Synchronized
    private fun addPlayer(playerConnection: PlayerConnection): PlayerConnection {
        connections.add(playerConnection)
        return playerConnection
    }

    @Synchronized
    fun update() {
        connections.removeIf(removePredicate)
        connections.forEach(updater)
    }

    fun getBedrockServer(): BedrockServer {
        return bedrockServer
    }

    companion object {
        val CODEC: BedrockPacketCodec = Bedrock_v575.V575_CODEC
    }
}