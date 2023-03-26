package org.jukeboxmc

import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import org.cloudburstmc.protocol.bedrock.data.PacketCompressionAlgorithm
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
import org.cloudburstmc.protocol.bedrock.packet.PlayerListPacket
import org.jukeboxmc.block.BlockRegistry
import org.jukeboxmc.blockentity.BlockEntityRegistry
import org.jukeboxmc.command.CommandSender
import org.jukeboxmc.config.Config
import org.jukeboxmc.config.ConfigType
import org.jukeboxmc.console.ConsoleSender
import org.jukeboxmc.console.TerminalConsole
import org.jukeboxmc.crafting.CraftingManager
import org.jukeboxmc.entity.EntityRegistry
import org.jukeboxmc.event.server.TpsChangeEvent
import org.jukeboxmc.event.world.WorldLoadEvent
import org.jukeboxmc.event.world.WorldLoadEvent.LoadType
import org.jukeboxmc.event.world.WorldUnloadEvent
import org.jukeboxmc.item.ItemRegistry
import org.jukeboxmc.item.enchantment.EnchantmentRegistry
import org.jukeboxmc.logger.Logger
import org.jukeboxmc.network.Network
import org.jukeboxmc.network.handler.HandlerRegistry
import org.jukeboxmc.player.GameMode
import org.jukeboxmc.player.Player
import org.jukeboxmc.player.info.DeviceInfo
import org.jukeboxmc.player.skin.Skin
import org.jukeboxmc.plugin.PluginLoadOrder
import org.jukeboxmc.plugin.PluginManager
import org.jukeboxmc.potion.EffectRegistry
import org.jukeboxmc.resourcepack.ResourcePackManager
import org.jukeboxmc.scheduler.Scheduler
import org.jukeboxmc.util.BiomeDefinitions
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.CreativeItems
import org.jukeboxmc.util.EntityIdentifiers
import org.jukeboxmc.util.IdentifierMapping
import org.jukeboxmc.util.ItemPalette
import org.jukeboxmc.util.ServerKiller
import org.jukeboxmc.world.Biome
import org.jukeboxmc.world.Difficulty
import org.jukeboxmc.world.Dimension
import org.jukeboxmc.world.World
import org.jukeboxmc.world.generator.FlatGenerator
import org.jukeboxmc.world.generator.Generator
import org.jukeboxmc.world.generator.NormalGenerator
import org.jukeboxmc.world.generator.populator.biome.BiomePopulatorRegistry
import java.io.File
import java.net.InetSocketAddress
import java.util.Locale
import java.util.UUID
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.LockSupport
import java.util.function.Consumer
import kotlin.concurrent.thread
import kotlin.math.roundToInt

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
    private lateinit var operatorConfig: Config
    val pluginFolder: File
    var serverAddress: String? = null
        private set
    var port = 0
        private set
    var maxPlayers = 0
        private set
    var viewDistance = 0
        private set
    var motd: String = ""
        private set
    var subMotd: String = ""
        private set
    lateinit var gameMode: GameMode
        private set
    lateinit var difficulty: Difficulty

    var defaultWorldName: String = ""
        private set
    var generatorName: String = ""
        private set
    var isOnlineMode = false
        private set
    var isForceResourcePacks = false
        private set
    lateinit var compressionAlgorithm: PacketCompressionAlgorithm
    val defaultWorld: World?
    private val players: MutableSet<Player> = HashSet()
    private val worlds: MutableMap<String, World> = HashMap()
    private val playerListEntry: Object2ObjectMap<UUID, PlayerListPacket.Entry> =
        Object2ObjectOpenHashMap()
    private val generators: MutableMap<Dimension?, Object2ObjectMap<String, Class<out Generator>>> =
        Object2ObjectOpenHashMap()
    var currentTick: Long
        private set
    private var lastTps = TICKS
    var currentTps: Long
        private set
    private var sleepBalance: Long = 0

    private val lock = Object()

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
        BlockPalette.init()
        BlockRegistry.init()
        BlockRegistry.initBlockProperties()
        CreativeItems.init()
        EntityIdentifiers.init()
        EntityRegistry.init()
        BiomeDefinitions.init()
        Biome.init()
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
        Runtime.getRuntime().addShutdownHook(
            thread(start = false) {
                runningState.set(false)
                shutdown()
            },
        )
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
                    synchronized(lock) {
                        lock.wait(
                            5.coerceAtLeast((nextTickTime - startTimeMillis - 25).toInt()).toLong(),
                        )
                    }
                }
                tick()
                nextTickTime += 50
            }
        } catch (e: InterruptedException) {
            Logger.instance.error("Error whilst waiting for next tick!", e)
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
            currentTps = (1 / lastTickTime.toDouble()).roundToInt().toLong()
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
        worlds.values.forEach { obj -> obj.close() }
        terminalConsole.stopConsole()
        scheduler.shutdown()
        network.shutdown()
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
        val serverConfig = Config(File(System.getProperty("user.dir"), "server.properties"), ConfigType.PROPERTIES).apply {
            addDefault("address", "0.0.0.0")
            addDefault("port", 19132)
            addDefault("max-players", 20)
            addDefault("view-distance", 32)
            addDefault("motd", "§bJukeboxMC")
            addDefault("sub-motd", "A fresh JukeboxMC Server")
            addDefault("gamemode", GameMode.CREATIVE.name)
            addDefault("default-difficulty", Difficulty.NORMAL.name)
            addDefault("default-world", "world")
            addDefault("generator", "flat")
            addDefault("online-mode", true)
            addDefault("forceResourcePacks", false)
            addDefault("compression", "zlib")
            save()
        }
        serverAddress = serverConfig.getString("address")
        port = serverConfig.getInt("port")
        maxPlayers = serverConfig.getInt("max-players")
        viewDistance = serverConfig.getInt("view-distance")
        motd = serverConfig.getString("motd")
        subMotd = serverConfig.getString("sub-motd")
        gameMode = GameMode.valueOf(serverConfig.getString("gamemode"))
        difficulty = Difficulty.valueOf(serverConfig.getString("default-difficulty"))
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
        operatorConfig.addDefault("operators", ArrayList<String>())
        operatorConfig.save()
    }

    fun isOperatorInFile(playerName: String): Boolean {
        return operatorConfig.exists("operators") && operatorConfig.getStringList("operators").contains(playerName)
    }

    fun addOperatorToFile(playerName: String) {
        if (operatorConfig.exists("operators") && !operatorConfig.getStringList("operators").contains(playerName)) {
            val operators = operatorConfig.getStringList("operators")
            operators.add(playerName)
            operatorConfig["operators"] = operators
            operatorConfig.save()
        }
    }

    fun removeOperatorFromFile(playerName: String?) {
        if (operatorConfig.exists("operators") && operatorConfig.getStringList("operators").contains(playerName)) {
            val operators = operatorConfig.getStringList("operators")
            operators.remove(playerName)
            operatorConfig["operators"] = operators
            operatorConfig.save()
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

    fun addPlayer(player: Player) {
        players.add(player)
    }

    fun removePlayer(player: Player) {
        players.removeIf { target: Player -> target.uuid == player.uuid }
    }

    val onlinePlayers: Collection<Player>
        get() = players

    fun getWorld(name: String): World? {
        var name = name
        if (name.isEmpty()) {
            return null
        }
        name = name.lowercase(Locale.getDefault())
        if (worlds.containsKey(name)) {
            return worlds[name]
        }
        val generatorMap: MutableMap<Dimension, String> = Object2ObjectOpenHashMap()
        generatorMap[Dimension.OVERWORLD] = generatorName
        return this.loadWorld(name, generatorMap)
    }

    fun loadWorld(name: String?): World? {
        val generatorMap: MutableMap<Dimension, String> = Object2ObjectOpenHashMap()
        generatorMap[Dimension.OVERWORLD] = generatorName
        return this.loadWorld(name, generatorMap)
    }

    fun loadWorld(name: String?, generatorMap: Map<Dimension, String>): World? {
        var name = name
        if (name.isNullOrEmpty()) {
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
        if (worldLoadEvent.isCancelled) {
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
        worldName: String,
        consumer: Consumer<Player> = Consumer { player: Player ->
            val world = getWorld(worldName)
            if (world != null) {
                if (world === defaultWorld || defaultWorld == null) {
                    player.playerConnection.disconnect("World was unloaded")
                } else {
                    player.teleport(defaultWorld.spawnLocation)
                }
            }
        },
    ) {
        val world = getWorld(worldName) ?: return
        val unloadWorldEvent = WorldUnloadEvent(world)
        pluginManager.callEvent(unloadWorldEvent)
        if (unloadWorldEvent.isCancelled) {
            return
        }
        for (player in unloadWorldEvent.world.players) {
            consumer.accept(player)
        }
        unloadWorldEvent.world.close()
        worlds.remove(worldName.lowercase(Locale.getDefault()))
        logger.info("World \"$worldName\" was unloaded")
    }

    fun getWorlds(): Collection<World> {
        return worlds.values
    }

    fun isWorldLoaded(name: String): Boolean {
        return worlds.containsKey(name.lowercase(Locale.getDefault()))
    }

    @Synchronized
    fun createGenerator(generatorName: String?, world: World, dimension: Dimension): Generator? {
        val generators = generators.getValue(dimension)
        val generator = generators[generatorName?.lowercase(Locale.getDefault())]
        return generator?.getConstructor(World::class.java)?.newInstance(world)
    }

    fun registerGenerator(name: String, clazz: Class<out Generator>, vararg dimensions: Dimension) {
        val lowercaseName = name.lowercase(Locale.getDefault())
        dimensions.forEach { dimension ->
            val generators = generators.computeIfAbsent(dimension) { k: Dimension? -> Object2ObjectOpenHashMap() }
            if (!generators.containsKey(lowercaseName)) {
                generators[lowercaseName] = clazz
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
        this.addToTabList(
            player.uuid,
            player.entityId,
            player.name,
            player.deviceInfo,
            player.xuid,
            player.skin,
        )
    }

    fun addToTabList(uuid: UUID?, entityId: Long, name: String?, deviceInfo: DeviceInfo, xuid: String?, skin: Skin) {
        val playerListPacket = PlayerListPacket()
        playerListPacket.action = PlayerListPacket.Action.ADD
        val entry = PlayerListPacket.Entry(uuid)
        entry.entityId = entityId
        entry.name = name
        entry.xuid = xuid
        entry.platformChatId = deviceInfo.deviceName
        entry.buildPlatform = deviceInfo.device.id
        entry.skin = skin.toNetwork()
        playerListPacket.entries.add(entry)
        playerListEntry[uuid] = entry
        this.broadcastPacket(playerListPacket)
    }

    fun removeFromTabList(player: Player) {
        val playerListPacket = PlayerListPacket()
        playerListPacket.action = PlayerListPacket.Action.REMOVE
        playerListPacket.entries.add(PlayerListPacket.Entry(player.uuid))
        this.broadcastPacket(playerListPacket)
        playerListEntry.remove(player.uuid)
    }

    fun getPlayerListEntry(): Object2ObjectMap<UUID, PlayerListPacket.Entry> {
        return playerListEntry
    }

    fun broadcastPacket(packet: BedrockPacket) {
        players.forEach { player: Player -> player.playerConnection.sendPacket(packet) }
    }

    fun broadcastPacket(players: Set<Player>, packet: BedrockPacket) {
        players.forEach { player: Player -> player.playerConnection.sendPacket(packet) }
    }

    fun broadcastMessage(message: String) {
        onlinePlayers.forEach { player ->
            player.sendMessage(message)
        }
        logger.info(message)
    }

    fun getPlayer(playerName: String): Player? {
        players.forEach { player ->
            if (player.name.equals(playerName, ignoreCase = true)) {
                return player
            }
        }
        return null
    }

    fun getPlayer(uuid: UUID): Player? {
        players.forEach { player ->
            if (player.uuid == uuid) {
                return player
            }
        }
        return null
    }

    fun dispatchCommand(commandSender: CommandSender, command: String) {
        pluginManager.getCommandManager().handleCommandInput(commandSender, "/$command")
    }

    companion object {
        private const val TICKS: Long = 20
        lateinit var instance: Server
            internal set
    }
}
