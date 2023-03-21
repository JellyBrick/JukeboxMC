package org.jukeboxmc

import java.util.UUID
import java.util.function.Consumer
import org.jukeboxmc.logger.Logger
import org.jukeboxmc.player.Player
import org.jukeboxmc.plugin.PluginManager
import org.jukeboxmc.scheduler.Scheduler
import org.jukeboxmc.world.Dimension
import org.jukeboxmc.world.World
import org.jukeboxmc.world.generator.Generator

/**
 * @author LucGamesYT
 * @version 1.0
 */
object JukeboxMC {
    private var server: Server? = null
    fun setServer(server: Server?) {
        JukeboxMC.server = server
    }

    val logger: Logger?
        get() = server.getLogger()
    val scheduler: Scheduler
        get() = server.getScheduler()
    val defaultWorld: World?
        get() = server.getDefaultWorld()
    val worlds: Collection<World?>
        get() = server!!.worlds

    fun getWorld(name: String?): World? {
        return server!!.getWorld(name)
    }

    fun loadWorld(name: String?): World? {
        return server!!.loadWorld(name)
    }

    fun loadWorld(name: String?, generatorMap: Map<Dimension?, String?>): World? {
        return server!!.loadWorld(name, generatorMap)
    }

    fun registerWorldGenerator(name: String, clazz: Class<out Generator>, vararg dimensions: Dimension?) {
        server!!.registerGenerator(name, clazz, *dimensions)
    }

    fun unloadWorld(name: String) {
        server!!.unloadWorld(name)
    }

    fun unloadWorld(worldName: String, consumer: Consumer<Player>) {
        server!!.unloadWorld(worldName, consumer)
    }

    fun isWorldLoaded(name: String): Boolean {
        return server!!.isWorldLoaded(name)
    }

    fun getPlayer(name: String?): Player? {
        return server!!.getPlayer(name)
    }

    fun getPlayer(uuid: UUID): Player? {
        return server!!.getPlayer(uuid)
    }

    val maxPlayers: Int
        get() = server.getMaxPlayers()
    val onlinePlayers: Collection<Player?>
        get() = server.getOnlinePlayers()
    val pluginManager: PluginManager
        get() = server.getPluginManager()
    val consoleSender: ConsoleSender
        get() = server!!.consoleSender
    val currentTps: Double
        get() = server.getCurrentTps()
    val address: InetSocketAddress
        get() = InetSocketAddress(server.getServerAddress(), server.getPort())
    val port: Int
        get() = server.getPort()

    fun dispatchCommand(consoleSender: ConsoleSender, command: String) {
        server!!.dispatchCommand(consoleSender, command)
    }

    fun broadcastMessage(message: String?) {
        server!!.broadcastMessage(message)
    }
}