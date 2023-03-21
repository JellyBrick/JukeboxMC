package org.jukeboxmc

import com.nukkitx.protocol.bedrock.BedrockPacket
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import java.io.File
import java.lang.reflect.InvocationTargetException
import java.util.Locale
import java.util.UUID
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Consumer
import org.jukeboxmc.block.BlockRegistry
import org.jukeboxmc.blockentity.BlockEntityRegistry
import org.jukeboxmc.command.CommandSender
import org.jukeboxmc.config.Config
import org.jukeboxmc.event.world.WorldLoadEvent.LoadType
import org.jukeboxmc.item.ItemRegistry
import org.jukeboxmc.item.enchantment.EnchantmentRegistry
import org.jukeboxmc.logger.Logger
import org.jukeboxmc.network.Network
import org.jukeboxmc.player.GameMode
import org.jukeboxmc.player.Player
import org.jukeboxmc.player.skin.Skin
import org.jukeboxmc.plugin.PluginManager
import org.jukeboxmc.resourcepack.ResourcePackManager
import org.jukeboxmc.scheduler.Scheduler
import org.jukeboxmc.util.BiomeDefinitions
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.CreativeItems
import org.jukeboxmc.util.EntityIdentifiers
import org.jukeboxmc.util.IdentifierMapping
import org.jukeboxmc.util.ItemPalette
import org.jukeboxmc.world.Biome
import org.jukeboxmc.world.Difficulty
import org.jukeboxmc.world.Dimension
import org.jukeboxmc.world.World
import org.jukeboxmc.world.generator.Generator
import org.jukeboxmc.world.generator.NormalGenerator

/**
 * @author LucGamesYT
 * @version 1.0
 */
class Server(logger: Logger) {
    val startTime: Long
    private val finishedState: AtomicBoolean
    private val runningState: AtomicBoolean
    private val mainThread: Thread
    val logger: Logger
    private val network: Network
    val scheduler: Scheduler
    private val resourcePackManager: ResourcePackManager
    private val consoleSender: ConsoleSender
    private val terminalConsole: TerminalConsole
    val pluginManager: PluginManager
    private val craftingManager: CraftingManager
    private var operatorConfig: Config? = null
    val pluginFolder: File
    var serverAddress: String? = null
        private set
    var port = 0
        private set
    var maxPlayers = 0
        private set
    var viewDistance = 0
        private set
    var motd: String? = null
        private set
    var subMotd: String? = null
        private set
    var gameMode: GameMode? = null
        private set
    private var difficulty: Difficulty? = null
    var defaultWorldName: String? = null
        private set
    var generatorName: String? = null
        private set
    var isOnlineMode = false
        private set
    var isForceResourcePacks = false
        private set
    private var compressionAlgorithm: PacketCompressionAlgorithm? = null
    val defaultWorld: World?
    private val players: MutableSet<Player?> = HashSet()
    private val worlds: MutableMap<String, World> = HashMap()
    private val playerListEntry: Object2ObjectMap<UUID?, PlayerListPacket.Entry> =
        Object2ObjectOpenHashMap<UUID?, PlayerListPacket.Entry>()
    private val generators: MutableMap<Dimension?, Object2ObjectMap<String, Class<out Generator>>> =
        EnumMap<Dimension, Object2ObjectMap<String, Class<out Generator>>>(
            Dimension::class.java
        )
    var currentTick: Long
        private set
    private var lastTps = TICKS
    var currentTps: Long
        private set
    private var sleepBalance: Long = 0

    init {
        instance = this
        JukeboxMC.setServer(this)
        this.logger = logger
        startTime = System.currentTimeMillis()
        Thread.currentThread().name = "JukeboxMC-Main"
        mainThread = Thread.currentThread()
        finishedState = AtomicBoolean(false)
        runningState = AtomicBoolean(true)
        currentTps = 20
        currentTick = 0
        initServerConfig()
        initOperatorConfig()
        consoleSender = ConsoleSender(this)
        terminalConsole = TerminalConsole(this)
        terminalConsole.startConsole()
        HandlerRegistry.init()
        ItemPalette.init()
        ItemRegistry.init()
        ItemRegistry.initItemProperties()
        IdentifierMapping.init()
        BlockRegistry.init()
        BlockRegistry.initBlockProperties()
        BlockPalette.init()
        CreativeItems.init()
        EntityIdentifiers.init()
        EntityRegistry.init()
        BiomeDefinitions.init()
        Biome.Companion.init()
        BlockEntityRegistry.init()
        EnchantmentRegistry.init()
        EffectRegistry.init()
        BiomePopulatorRegistry.init()
        scheduler = Scheduler(this)
        resourcePackManager = ResourcePackManager(logger)
        resourcePackManager.loadResourcePacks()
        craftingManager = CraftingManager()
        pluginFolder = File("./plugins")
        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs()
        }
        pluginManager = PluginManager(this)
        pluginManager.enableAllPlugins(PluginLoadOrder.STARTUP)
        registerGenerator("flat", FlatGenerator::class.java, Dimension.OVERWORLD)
        registerGenerator("normal", NormalGenerator::class.java, Dimension.OVERWORLD)
        registerGenerator("empty", FlatGenerator::class.java, Dimension.OVERWORLD)
        registerGenerator("empty", FlatGenerator::class.java, Dimension.NETHER)
        registerGenerator("empty", FlatGenerator::class.java, Dimension.THE_END)
        defaultWorld = getWorld(defaultWorldName)
        pluginManager.enableAllPlugins(PluginLoadOrder.POSTWORLD)
        network = Network(this, InetSocketAddress(serverAddress, port))
        this.logger.info("JukeboxMC started in " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + " seconds!")
        finishedState.set(true)
        startTick()
        shutdown()
    }

    private fun startTick() {
        var nextTickTime = System.currentTimeMillis()
        try {
            while (runningState.get()) {
                val startTimeMillis = System.currentTimeMillis()
                if (nextTickTime - startTimeMillis > 25) {
                    synchronized(this) { this.wait(Math.max(5, nextTickTime - startTimeMillis - 25)) }
                }
                tick()
                nextTickTime += 50
            }
        } catch (e: InterruptedException) {
            Logger.Companion.getInstance().error("Error whilst waiting for next tick!", e)
        }
    }

    private fun tick() {
        val skipNanos = TimeUnit.SECONDS.toNanos(1) / TICKS
        var lastTickTime: Float
        while (runningState.get()) {
            val internalDiffTime = System.nanoTime()
            currentTick++
            scheduler.onTick(currentTick)
            network.update()
            for (value in worlds.values) {
                value.update(currentTick)
            }
            if (!runningState.get()) {
                break
            }
            val startSleep = System.nanoTime()
            var diff = startSleep - internalDiffTime
            if (diff <= skipNanos) {
                val sleepNeeded = skipNanos - diff - sleepBalance
                sleepBalance = 0
                LockSupport.parkNanos(sleepNeeded)
                val endSleep = System.nanoTime()
                val sleptFor = endSleep - startSleep
                diff = skipNanos
                if (sleptFor > sleepNeeded) {
                    sleepBalance = sleptFor - sleepNeeded
                }
            }
            if (currentTick % (20 * 20) == 0L) {
                System.gc()
            }
            lastTickTime = diff.toFloat() / TimeUnit.SECONDS.toNanos(1)
            currentTps = Math.round(1 / lastTickTime.toDouble()).toInt().toLong()
            if (currentTps != lastTps) {
                pluginManager.callEvent(TpsChangeEvent(this, lastTps, currentTick))
            }
            lastTps = currentTps
        }
    }

    fun shutdown() {
        if (!runningState.get()) {
            return
        }
        logger.info("Shutdown server...")
        runningState.set(false)
        pluginManager.disableAllPlugins()
        logger.info("Save all worlds...")
        for (world in worlds.values) {
            world.saveChunks(Dimension.OVERWORLD).join()
            world.saveChunks(Dimension.NETHER).join()
            world.saveChunks(Dimension.THE_END).join()
            logger.info("The world \"" + world.name + "\" was saved!")
        }
        worlds.values.forEach(Consumer { obj: World -> obj.close() })
        terminalConsole.stopConsole()
        scheduler.shutdown()
        network.bedrockServer.close(true)
        logger.info("Stopping other threads")
        for (thread in Thread.getAllStackTraces().keys) {
            if (thread.isAlive) {
                thread.interrupt()
            }
        }
        val serverKiller = ServerKiller(logger)
        serverKiller.start()
    }

    private fun initServerConfig() {
        val serverConfig = Config(File(System.getProperty("user.dir"), "properties.json"), ConfigType.JSON)
        serverConfig.addDefault("address", "0.0.0.0")
        serverConfig.addDefault("port", 19132)
        serverConfig.addDefault("max-players", 20)
        serverConfig.addDefault("view-distance", 32)
        serverConfig.addDefault("motd", "§bJukeboxMC")
        serverConfig.addDefault("sub-motd", "A fresh JukeboxMC Server")
        serverConfig.addDefault("gamemode", GameMode.CREATIVE.name)
        serverConfig.addDefault("default-difficulty", Difficulty.NORMAL.name)
        serverConfig.addDefault("default-world", "world")
        serverConfig.addDefault("generator", "flat")
        serverConfig.addDefault("online-mode", true)
        serverConfig.addDefault("forceResourcePacks", false)
        serverConfig.addDefault("compression", "zlib")
        serverConfig.save()
        serverAddress = serverConfig.getString("address")
        port = serverConfig.getInt("port")
        maxPlayers = serverConfig.getInt("max-players")
        viewDistance = serverConfig.getInt("view-distance")
        motd = serverConfig.getString("motd")
        subMotd = serverConfig.getString("sub-motd")
        gameMode = GameMode.valueOf(serverConfig.getString("gamemode")!!)
        difficulty = Difficulty.valueOf(serverConfig.getString("default-difficulty")!!)
        defaultWorldName = serverConfig.getString("default-world")
        generatorName = serverConfig.getString("generator")
        isOnlineMode = serverConfig.getBoolean("online-mode")
        isForceResourcePacks = serverConfig.getBoolean("forceResourcePacks")
        val compression = serverConfig.getString("compression")
        compressionAlgorithm = PacketCompressionAlgorithm.ZLIB
        for (algorithm in PacketCompressionAlgorithm.values()) {
            if (algorithm.name.equals(compression, ignoreCase = true)) {
                compressionAlgorithm = algorithm
                break
            }
        }
    }

    private fun initOperatorConfig() {
        operatorConfig = Config(File(System.getProperty("user.dir"), "operators.json"), ConfigType.JSON)
        operatorConfig!!.addDefault("operators", ArrayList<String>())
        operatorConfig!!.save()
    }

    fun isOperatorInFile(playerName: String?): Boolean {
        return operatorConfig!!.exists("operators") && operatorConfig!!.getStringList("operators").contains(playerName)
    }

    fun addOperatorToFile(playerName: String?) {
        if (operatorConfig!!.exists("operators") && !operatorConfig!!.getStringList("operators").contains(playerName)) {
            val operators = operatorConfig!!.getStringList("operators")
            operators!!.add(playerName)
            operatorConfig!!["operators"] = operators
            operatorConfig!!.save()
        }
    }

    fun removeOperatorFromFile(playerName: String?) {
        if (operatorConfig!!.exists("operators") && operatorConfig!!.getStringList("operators").contains(playerName)) {
            val operators = operatorConfig!!.getStringList("operators")
            operators!!.remove(playerName)
            operatorConfig!!["operators"] = operators
            operatorConfig!!.save()
        }
    }

    fun getFinishedState(): AtomicBoolean {
        return finishedState
    }

    fun getRunningState(): AtomicBoolean {
        return runningState
    }

    fun isMainThread(): Boolean {
        return Thread.currentThread().id == mainThread.id
    }

    fun getMainThread(): Thread {
        return mainThread
    }

    fun getDifficulty(): Difficulty? {
        return difficulty
    }

    fun getCompressionAlgorithm(): PacketCompressionAlgorithm? {
        return compressionAlgorithm
    }

    fun addPlayer(player: Player?) {
        players.add(player)
    }

    fun removePlayer(player: Player) {
        players.removeIf { target: Player? -> target.getUUID() == player.uuid }
    }

    val onlinePlayers: Collection<Player?>
        get() = players

    fun getWorld(name: String?): World? {
        var name = name
        if (name == null || name.isEmpty()) {
            return null
        }
        name = name.lowercase(Locale.getDefault())
        if (worlds.containsKey(name)) {
            return worlds[name]
        }
        val generatorMap: MutableMap<Dimension?, String?> = EnumMap<Dimension, String>(
            Dimension::class.java
        )
        generatorMap[Dimension.OVERWORLD] = generatorName
        return this.loadWorld(name, generatorMap)
    }

    fun loadWorld(name: String?): World? {
        val generatorMap: MutableMap<Dimension?, String?> = EnumMap<Dimension, String>(
            Dimension::class.java
        )
        generatorMap[Dimension.OVERWORLD] = generatorName
        return this.loadWorld(name, generatorMap)
    }

    fun loadWorld(name: String?, generatorMap: Map<Dimension?, String?>): World? {
        var name = name
        if (name == null || name.isEmpty()) {
            return null
        }
        val file = File("./worlds", name)
        val worldExists = file.exists()
        name = name.lowercase(Locale.getDefault())
        if (worlds.containsKey(name)) {
            return worlds[name]
        }
        val world = World(name, this, generatorMap)
        val worldLoadEvent = WorldLoadEvent(world, if (worldExists) LoadType.LOAD else LoadType.CREATE)
        pluginManager.callEvent(worldLoadEvent)
        if (worldLoadEvent.isCancelled()) {
            return null
        }
        if (!worlds.containsKey(name)) {
            worlds[name] = world
            return world
        }
        return null
    }

    @JvmOverloads
    fun unloadWorld(
        worldName: String, consumer: Consumer<Player> = Consumer { player: Player ->
            val world = getWorld(worldName)
            if (world != null) {
                if (world === defaultWorld || defaultWorld == null) {
                    player.playerConnection.disconnect("World was unloaded")
                } else {
                    player.teleport(defaultWorld.spawnLocation)
                }
            }
        }
    ) {
        val world = getWorld(worldName)
        val unloadWorldEvent = WorldUnloadEvent(world)
        pluginManager.callEvent(unloadWorldEvent)
        if (unloadWorldEvent.isCancelled()) {
            return
        }
        if (unloadWorldEvent.getWorld() != null) {
            for (player in unloadWorldEvent.getWorld().getPlayers()) {
                consumer.accept(player)
            }
            unloadWorldEvent.getWorld().close()
            worlds.remove(worldName.lowercase(Locale.getDefault()))
            logger.info("World \"$worldName\" was unloaded")
        } else {
            logger.warn("The world \"$worldName\" was not found")
        }
    }

    fun getWorlds(): Collection<World> {
        return worlds.values
    }

    fun isWorldLoaded(name: String): Boolean {
        return worlds.containsKey(name.lowercase(Locale.getDefault()))
    }

    @Synchronized
    fun createGenerator(generatorName: String, world: World?, dimension: Dimension?): Generator? {
        val generators = generators[dimension]!!
        val generator = generators[generatorName.lowercase(Locale.getDefault())]
        return if (generator != null) {
            try {
                generator.getConstructor(World::class.java).newInstance(world)
            } catch (e: InvocationTargetException) {
                throw RuntimeException(e)
            } catch (e: InstantiationException) {
                throw RuntimeException(e)
            } catch (e: IllegalAccessException) {
                throw RuntimeException(e)
            } catch (e: NoSuchMethodException) {
                throw RuntimeException(e)
            }
        } else null
    }

    fun registerGenerator(name: String, clazz: Class<out Generator>, vararg dimensions: Dimension) {
        var name = name
        name = name.lowercase(Locale.getDefault())
        for (dimension in dimensions) {
            val generators = generators.computeIfAbsent(dimension) { k: Dimension? -> Object2ObjectOpenHashMap() }
            if (!generators.containsKey(name)) {
                generators[name] = clazz
            }
        }
    }

    fun getResourcePackManager(): ResourcePackManager {
        return resourcePackManager
    }

    fun getCraftingManager(): CraftingManager {
        return craftingManager
    }

    fun getConsoleSender(): ConsoleSender {
        return consoleSender
    }

    fun addToTabList(player: Player) {
        this.addToTabList(player.uuid, player.entityId, player.name, player.deviceInfo, player.xuid, player.skin)
    }

    fun addToTabList(uuid: UUID?, entityId: Long, name: String?, deviceInfo: DeviceInfo, xuid: String?, skin: Skin) {
        val playerListPacket = PlayerListPacket()
        playerListPacket.setAction(PlayerListPacket.Action.ADD)
        val entry: PlayerListPacket.Entry = PlayerListPacket.Entry(uuid)
        entry.setEntityId(entityId)
        entry.setName(name)
        entry.setXuid(xuid)
        entry.setPlatformChatId(deviceInfo.getDeviceName())
        entry.setBuildPlatform(deviceInfo.getDevice().getId())
        entry.setSkin(skin.toNetwork())
        playerListPacket.getEntries().add(entry)
        playerListEntry[uuid] = entry
        this.broadcastPacket(playerListPacket)
    }

    fun removeFromTabList(player: Player) {
        val playerListPacket = PlayerListPacket()
        playerListPacket.setAction(PlayerListPacket.Action.REMOVE)
        playerListPacket.getEntries().add(PlayerListPacket.Entry(player.uuid))
        this.broadcastPacket(playerListPacket)
        playerListEntry.remove(player.uuid)
    }

    fun getPlayerListEntry(): Object2ObjectMap<UUID?, PlayerListPacket.Entry> {
        return playerListEntry
    }

    fun broadcastPacket(packet: BedrockPacket?) {
        players.forEach(Consumer { player: Player? -> player.getPlayerConnection().sendPacket(packet) })
    }

    fun broadcastPacket(players: Set<Player?>, packet: BedrockPacket?) {
        players.forEach(Consumer { player: Player? -> player.getPlayerConnection().sendPacket(packet) })
    }

    fun broadcastMessage(message: String?) {
        for (player in onlinePlayers) {
            player!!.sendMessage(message)
        }
        logger.info(message)
    }

    fun getPlayer(playerName: String?): Player? {
        for (player in ArrayList(players)) {
            if (player!!.name.equals(playerName, ignoreCase = true)) {
                return player
            }
        }
        return null
    }

    fun getPlayer(uuid: UUID): Player? {
        for (player in ArrayList(players)) {
            if (player.getUUID() == uuid) {
                return player
            }
        }
        return null
    }

    fun dispatchCommand(commandSender: CommandSender, command: String) {
        pluginManager.commandManager.handleCommandInput(commandSender, "/$command")
    }

    companion object {
        private const val TICKS: Long = 20
        var instance: Server
            private set
    }
}