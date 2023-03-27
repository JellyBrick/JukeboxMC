package org.jukeboxmc.network

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioDatagramChannel
import org.cloudburstmc.netty.channel.raknet.RakChannelFactory
import org.cloudburstmc.netty.channel.raknet.config.RakChannelOption
import org.cloudburstmc.protocol.bedrock.BedrockPong
import org.cloudburstmc.protocol.bedrock.BedrockServerSession
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec
import org.cloudburstmc.protocol.bedrock.codec.v575.Bedrock_v575
import org.cloudburstmc.protocol.bedrock.netty.initializer.BedrockServerInitializer
import org.jukeboxmc.Server
import org.jukeboxmc.player.PlayerConnection
import java.net.InetSocketAddress
import java.util.function.Consumer

/**
 * @author LucGamesYT
 * @version 1.0
 */
class Network(val server: Server, val inetSocketAddress: InetSocketAddress) {
    private val bedrockPong: BedrockPong = BedrockPong().apply {
        edition("MCPE")
        gameType(server.gameMode.identifier)
        motd(server.motd)
        subMotd(server.subMotd)
        playerCount(server.onlinePlayers.size)
        maximumPlayerCount(server.maxPlayers)
        ipv4Port(inetSocketAddress.port)
        nintendoLimited(false)
        protocolVersion(CODEC.protocolVersion)
        version(CODEC.minecraftVersion)
    }
    private val connections: MutableSet<PlayerConnection> = HashSet()
    private val updater: Consumer<PlayerConnection> =
        Consumer<PlayerConnection> { obj: PlayerConnection -> obj.update() }

    private val channels: MutableList<Channel> = mutableListOf()

    init {
        try {
            val bootstrap: ServerBootstrap = ServerBootstrap()
                .channelFactory(RakChannelFactory.server(NioDatagramChannel::class.java)) // TODO: epoll, kqueue
                .group(NioEventLoopGroup())
//                .option(RakChannelOption.RAK_HANDLE_PING, true) // TODO: dynamic
                .option(RakChannelOption.RAK_ADVERTISEMENT, bedrockPong.toByteBuf())
                .childHandler(object : BedrockServerInitializer() {
                    override fun initSession(bedrockServerSession: BedrockServerSession) {
                        val playerConnection = addPlayer(PlayerConnection(server, bedrockServerSession))
                        server.addPlayer(playerConnection.player)
                    }
                })
            channels.add(
                bootstrap.bind(inetSocketAddress)
                    .awaitUninterruptibly()
                    .channel(),
            )
            server.logger.info("Server started successfully at " + this.inetSocketAddress.hostString + ":" + this.inetSocketAddress.port + "!")
        } catch (e: Exception) {
            server.logger.error("Could not start server! Is there already running something on this port?", e)
        }
    }

    fun onQuery(inetSocketAddress: InetSocketAddress): BedrockPong {
//        bedrockPong.edition = "MCPE"
//        bedrockPong.gameType = server.gameMode.identifier
//        bedrockPong.motd = server.motd
//        bedrockPong.subMotd = server.subMotd
//        bedrockPong.playerCount = server.onlinePlayers.size
//        bedrockPong.maximumPlayerCount = server.maxPlayers
//        bedrockPong.ipv4Port = this.inetSocketAddress.port
//        bedrockPong.isNintendoLimited = false
//        bedrockPong.protocolVersion = CODEC.protocolVersion
//        bedrockPong.version = CODEC.minecraftVersion
        return bedrockPong.apply {
            edition("MCPE")
            gameType(server.gameMode.identifier)
            motd(server.motd)
            subMotd(server.subMotd)
            maximumPlayerCount(server.onlinePlayers.size)
            maximumPlayerCount(server.maxPlayers)
            ipv4Port(inetSocketAddress.port)
            nintendoLimited(false)
            protocolVersion(CODEC.protocolVersion)
            version(CODEC.minecraftVersion)
        }
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

    fun shutdown() {
        val playerConnectionIterator = connections.iterator()
        while (playerConnectionIterator.hasNext()) {
            val playerConnection = playerConnectionIterator.next()
            playerConnection.disconnect("Server closed") // TODO: configurable
        }
        channels.forEach(Channel::close)
    }

    companion object {
        val CODEC: BedrockCodec = Bedrock_v575.CODEC
    }
}
