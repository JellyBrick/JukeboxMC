package org.jukeboxmc

import org.jukeboxmc.console.ConsoleSender
import org.jukeboxmc.logger.Logger
import org.jukeboxmc.player.Player
import org.jukeboxmc.plugin.PluginManager
import org.jukeboxmc.scheduler.Scheduler
import org.jukeboxmc.world.Dimension
import org.jukeboxmc.world.World
import org.jukeboxmc.world.generator.Generator
import java.net.InetSocketAddress
import java.util.UUID
import java.util.function.Consumer

/**
 * @author LucGamesYT
 * @version 1.0
 */
object JukeboxMC {
    lateinit var server: Server
        private set

    fun setServer(server: Server) {
        JukeboxMC.server = server
    }

    val logger: Logger
        get() = server.logger
    val scheduler: Scheduler
        get() = server.scheduler
    val defaultWorld: World?
        get() = server.defaultWorld
    val worlds: Collection<World>
        get() = server.getWorlds()

    fun getWorld(name: String): World? {
        return server.getWorld(name)
    }

    fun loadWorld(name: String?): World? {
        return server.loadWorld(name)
    }

    fun loadWorld(name: String?, generatorMap: Map<Dimension, String>): World? {
        return server.loadWorld(name, generatorMap)
    }

    fun registerWorldGenerator(name: String, clazz: Class<out Generator>, vararg dimensions: Dimension) {
        server.registerGenerator(name, clazz, *dimensions)
    }

    fun unloadWorld(name: String) {
        server.unloadWorld(name)
    }

    fun unloadWorld(worldName: String, consumer: Consumer<Player>) {
        server.unloadWorld(worldName, consumer)
    }

    fun isWorldLoaded(name: String): Boolean {
        return server.isWorldLoaded(name)
    }

    fun getPlayer(name: String): Player? {
        return server.getPlayer(name)
    }

    fun getPlayer(uuid: UUID): Player? {
        return server.getPlayer(uuid)
    }

    val maxPlayers: Int
        get() = server.maxPlayers
    val onlinePlayers: Collection<Player>
        get() = server.onlinePlayers
    val pluginManager: PluginManager
        get() = server.pluginManager
    val consoleSender: ConsoleSender
        get() = server.getConsoleSender()
    val currentTps: Double
        get() = server.currentTps.toDouble()
    val address: InetSocketAddress
        get() = InetSocketAddress(server.serverAddress, server.port)
    val port: Int
        get() = server.port

    fun dispatchCommand(consoleSender: ConsoleSender, command: String) {
        server.dispatchCommand(consoleSender, command)
    }

    fun broadcastMessage(message: String) {
        server.broadcastMessage(message)
    }
}
