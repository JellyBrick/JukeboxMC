package org.jukeboxmc.player

import com.nukkitx.nbt.NbtMap
import com.nukkitx.protocol.bedrock.BedrockPacket
import com.nukkitx.protocol.bedrock.packet.SetEntityDataPacket
import com.nukkitx.protocol.bedrock.packet.StartGamePacket
import io.netty.buffer.ByteBuf
import java.util.UUID
import java.util.function.BiConsumer
import java.util.function.Consumer
import org.jukeboxmc.Server
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.network.Network
import org.jukeboxmc.network.handler.PacketHandler
import org.jukeboxmc.util.BiomeDefinitions
import org.jukeboxmc.util.CreativeItems
import org.jukeboxmc.util.EntityIdentifiers
import org.jukeboxmc.util.ItemPalette

/**
 * @author LucGamesYT
 * @version 1.0
 */
class PlayerConnection(val server: Server, session: BedrockServerSession) {
    private val session: BedrockServerSession
    private val loggedIn: AtomicBoolean
    private val spawned: AtomicBoolean
    var loginData: LoginData? = null
        set(loginData) {
            if (this.loginData == null) {
                field = loginData
                player.name = loginData.getDisplayName()
                player.nameTag = loginData.getDisplayName()
                player.uuid = loginData.getUuid()
                player.skin = loginData.getSkin()
                player.deviceInfo = loginData.getDeviceInfo()
            }
        }
    val player: Player
    private var disconnectMessage: String? = null
    private val playerChunkManager: PlayerChunkManager

    init {
        this.session = session
        this.session.getHardcodedBlockingId().set(Item.Companion.create<Item>(ItemType.SHIELD).getRuntimeId())
        this.session.addDisconnectHandler(Consumer<DisconnectReason> { disconnectReason: DisconnectReason? ->
            server.scheduler.execute {
                onDisconnect(
                    disconnectReason
                )
            }
        })
        loggedIn = AtomicBoolean(false)
        spawned = AtomicBoolean(false)
        player = Player(server, this)
        playerChunkManager = PlayerChunkManager(player)
        session.setPacketCodec(Network.Companion.CODEC)
        session.setBatchHandler(BatchHandler { bedrockSession: BedrockSession?, byteBuf: ByteBuf?, packets: Collection<BedrockPacket> ->
            for (packet in packets) {
                try {
                    server.scheduler.execute {
                        val packetReceiveEvent = PacketReceiveEvent(player, packet)
                        server.pluginManager.callEvent(packetReceiveEvent)
                        if (packetReceiveEvent.isCancelled()) {
                            return@execute
                        }
                        val packetHandler =
                            HandlerRegistry.getPacketHandler(packetReceiveEvent.getPacket().javaClass) as PacketHandler<BedrockPacket>
                        if (packetHandler != null) {
                            packetHandler.handle(packetReceiveEvent.getPacket(), server, player)
                        } else {
                            server.logger.info("Handler missing for packet: " + packet.javaClass.simpleName)
                        }
                    }
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()
                }
            }
        })
    }

    fun update() {
        if (isClosed || !loggedIn.get()) {
            return
        }
        if (spawned.get()) {
            playerChunkManager.queueNewChunks()
        }
        playerChunkManager.sendQueued()
        if (playerChunkManager.getChunksSent() >= 25 && !spawned.get() && player.teleportLocation == null) {
            doFirstSpawn()
        }
    }

    private fun onDisconnect(disconnectReason: DisconnectReason?) {
        server.removePlayer(player)
        player.world.removeEntity(player)
        player.chunk.removeEntity(player)
        player.inventory.removeViewer(player)
        player.armorInventory.removeViewer(player)
        player.cursorInventory.removeViewer(player)
        player.creativeItemCacheInventory.removeViewer(player)
        player.craftingGridInventory.removeViewer(player)
        player.craftingTableInventory.removeViewer(player)
        player.cartographyTableInventory.removeViewer(player)
        player.smithingTableInventory.removeViewer(player)
        player.anvilInventory.removeViewer(player)
        //this.player.getEnderChestInventory().removeViewer( this.player );
        player.stoneCutterInventory.removeViewer(player)
        player.grindstoneInventory.removeViewer(player)
        player.offHandInventory.removeViewer(player)
        server.removeFromTabList(player)
        playerChunkManager.clear()
        player.close()
        val playerQuitEvent = PlayerQuitEvent(player, "§e" + player.name + " left the game")
        Server.Companion.getInstance().getPluginManager().callEvent(playerQuitEvent)
        if (playerQuitEvent.getQuitMessage() != null && !playerQuitEvent.getQuitMessage().isEmpty()) {
            server.broadcastMessage(playerQuitEvent.getQuitMessage())
        }
        server.logger.info(
            player.name + " logged out reason: " + if (disconnectMessage == null) parseDisconnectMessage(
                disconnectReason
            ) else disconnectMessage
        )
    }

    private fun doFirstSpawn() {
        spawned.set(true)
        player.world.addEntity(player)
        val setEntityDataPacket = SetEntityDataPacket()
        setEntityDataPacket.runtimeEntityId = player.entityId
        setEntityDataPacket.metadata.putAll(player.metadata.entityDataMap)
        setEntityDataPacket.tick = server.currentTick
        sendPacket(setEntityDataPacket)
        val adventureSettings = player.adventureSettings
        if (server.isOperatorInFile(player.name)) {
            adventureSettings[AdventureSettings.Type.OPERATOR] = true
        }
        adventureSettings[AdventureSettings.Type.WORLD_IMMUTABLE] = player.gameMode.ordinal == 3
        adventureSettings[AdventureSettings.Type.ALLOW_FLIGHT] = player.gameMode.ordinal > 0
        adventureSettings[AdventureSettings.Type.NO_CLIP] = player.gameMode.ordinal == 3
        adventureSettings[AdventureSettings.Type.FLYING] = player.gameMode.ordinal == 3
        adventureSettings[AdventureSettings.Type.ATTACK_MOBS] = player.gameMode.ordinal < 2
        adventureSettings[AdventureSettings.Type.ATTACK_PLAYERS] = player.gameMode.ordinal < 2
        adventureSettings[AdventureSettings.Type.NO_PVM] = player.gameMode.ordinal == 3
        adventureSettings.update()
        player.sendCommandData()
        val updateAttributesPacket = UpdateAttributesPacket()
        updateAttributesPacket.setRuntimeEntityId(player.entityId)
        for (attribute in player.attributes) {
            updateAttributesPacket.getAttributes().add(attribute.toNetwork())
        }
        updateAttributesPacket.setTick(server.currentTick)
        sendPacket(updateAttributesPacket)
        server.addToTabList(player)
        if (server.onlinePlayers.size > 1) {
            val playerListPacket = PlayerListPacket()
            playerListPacket.setAction(PlayerListPacket.Action.ADD)
            server.playerListEntry.forEach(BiConsumer<UUID, PlayerListPacket.Entry> { uuid: UUID, entry: PlayerListPacket.Entry? ->
                if (uuid !== player.uuid) {
                    playerListPacket.getEntries().add(entry)
                }
            })
            player.playerConnection.sendPacket(playerListPacket)
        }
        player.inventory.addViewer(player)
        player.inventory.sendContents(player)
        player.cursorInventory.addViewer(player)
        player.cursorInventory.sendContents(player)
        player.armorInventory.addViewer(player)
        player.armorInventory.sendContents(player)
        val playStatusPacket = PlayStatusPacket()
        playStatusPacket.setStatus(PlayStatusPacket.Status.PLAYER_SPAWN)
        sendPacket(playStatusPacket)
        val setTimePacket = SetTimePacket()
        setTimePacket.setTime(player.world.worldTime)
        sendPacket(setTimePacket)
        for (onlinePlayer in server.onlinePlayers) {
            if (onlinePlayer != null && onlinePlayer.dimension == player.dimension) {
                player.spawn(onlinePlayer)
                onlinePlayer.spawn(player)
            }
        }
        server.logger.info(
            player.name + " logged in [World=" + player.world.name + ", X=" +
                    player.blockX + ", Y=" + player.blockY + ", Z=" + player.blockZ +
                    ", Dimension=" + player.location.dimension.name + "]"
        )
    }

    fun initializePlayer() {
        loggedIn.set(true)
        val startGamePacket = StartGamePacket()
        startGamePacket.serverEngine = "JukeboxMC"
        startGamePacket.uniqueEntityId = player.entityId
        startGamePacket.runtimeEntityId = player.entityId
        startGamePacket.playerGameType = player.gameMode.toGameType()
        startGamePacket.playerPosition = player.location.toVector3f().add(0f, 2f, 0f) //TODO
        startGamePacket.defaultSpawn = player.location.toVector3i().add(0, 2, 0) //TODO
        startGamePacket.rotation = Vector2f.from(player.pitch, player.yaw)
        startGamePacket.seed = 0L //TODO
        startGamePacket.dimensionId = player.location.dimension.ordinal
        startGamePacket.isTrustingPlayers = true
        startGamePacket.levelGameType = server.gameMode.toGameType()
        startGamePacket.difficulty = player.world.difficulty.ordinal
        startGamePacket.isAchievementsDisabled = true
        startGamePacket.dayCycleStopTime = 0
        startGamePacket.rainLevel = 0f
        startGamePacket.lightningLevel = 0f
        startGamePacket.isCommandsEnabled = true
        startGamePacket.isMultiplayerGame = true
        startGamePacket.isBroadcastingToLan = true
        startGamePacket.gamerules.addAll(player.world.gameRules.gameRules)
        startGamePacket.levelId = ""
        startGamePacket.levelName = player.world.name
        startGamePacket.generatorId = 1
        startGamePacket.itemEntries = ItemPalette.getEntries()
        startGamePacket.xblBroadcastMode = GamePublishSetting.PUBLIC
        startGamePacket.platformBroadcastMode = GamePublishSetting.PUBLIC
        startGamePacket.defaultPlayerPermission = PlayerPermission.MEMBER
        startGamePacket.serverChunkTickRange = 4
        startGamePacket.vanillaVersion = Network.Companion.CODEC.getMinecraftVersion()
        startGamePacket.premiumWorldTemplateId = ""
        startGamePacket.multiplayerCorrelationId = ""
        startGamePacket.isInventoriesServerAuthoritative = true
        startGamePacket.playerMovementSettings = SYNCED_PLAYER_MOVEMENT_SETTINGS
        startGamePacket.blockRegistryChecksum = 0L
        startGamePacket.playerPropertyData = NbtMap.EMPTY
        startGamePacket.worldTemplateId = UUID(0, 0)
        startGamePacket.chatRestrictionLevel = ChatRestrictionLevel.NONE
        startGamePacket.isDisablingPlayerInteractions = false
        startGamePacket.isClientSideGenerationEnabled = false
        sendPacket(startGamePacket)
        val availableEntityIdentifiersPacket = AvailableEntityIdentifiersPacket()
        availableEntityIdentifiersPacket.setIdentifiers(EntityIdentifiers.getIdentifiers())
        sendPacket(availableEntityIdentifiersPacket)
        val biomeDefinitionListPacket = BiomeDefinitionListPacket()
        biomeDefinitionListPacket.setDefinitions(BiomeDefinitions.getBiomeDefinitions())
        sendPacket(biomeDefinitionListPacket)
        val creativeContentPacket = CreativeContentPacket()
        creativeContentPacket.setContents(CreativeItems.getCreativeItems().toTypedArray())
        sendPacket(creativeContentPacket)
        val craftingManager: CraftingManager = server.craftingManager
        val craftingDataPacket = CraftingDataPacket()
        craftingDataPacket.getCraftingData().addAll(craftingManager.getCraftingData())
        craftingDataPacket.getPotionMixData().addAll(craftingDataPacket.getPotionMixData())
        craftingDataPacket.getContainerMixData().addAll(craftingManager.getContainerMixData())
        craftingDataPacket.setCleanRecipes(true)
        sendPacket(craftingDataPacket)
    }

    private fun parseDisconnectMessage(disconnectReason: DisconnectReason?): String {
        return when (if (disconnectReason != null) disconnectReason else DisconnectReason.DISCONNECTED) {
            DisconnectReason.ALREADY_CONNECTED -> {
                "Already connected"
            }

            DisconnectReason.SHUTTING_DOWN -> {
                "Shutdown"
            }

            else -> {
                "Disconnect"
            }
        }
    }

    fun disconnect() {
        onDisconnect(DisconnectReason.DISCONNECTED)
        session.disconnect()
    }

    fun disconnect(message: String) {
        session.disconnect(message.also { disconnectMessage = it })
        onDisconnect(null)
    }

    fun disconnect(message: String, hideReason: Boolean) {
        session.disconnect(message.also { disconnectMessage = it }, hideReason)
        onDisconnect(null)
    }

    fun sendPlayStatus(status: PlayStatusPacket.Status?) {
        val playStatusPacket = PlayStatusPacket()
        playStatusPacket.setStatus(status)
        sendPacketImmediately(playStatusPacket)
    }

    fun sendPacket(packet: BedrockPacket?) {
        if (!isClosed && session.getPacketCodec() != null) {
            val packetSendEvent = PacketSendEvent(player, packet)
            Server.Companion.getInstance().getPluginManager().callEvent(packetSendEvent)
            if (packetSendEvent.isCancelled()) {
                return
            }
            session.sendPacket(packetSendEvent.getPacket())
        }
    }

    fun sendPacketImmediately(packet: BedrockPacket) {
        if (!isClosed) {
            session.sendPacketImmediately(packet)
        }
    }

    val isClosed: Boolean
        get() = session.isClosed()

    fun getSession(): BedrockServerSession {
        return session
    }

    fun isLoggedIn(): Boolean {
        return loggedIn.get()
    }

    fun isSpawned(): Boolean {
        return spawned.get()
    }

    fun getPlayerChunkManager(): PlayerChunkManager {
        return playerChunkManager
    }

    companion object {
        private val SYNCED_PLAYER_MOVEMENT_SETTINGS: SyncedPlayerMovementSettings = SyncedPlayerMovementSettings()

        init {
            SYNCED_PLAYER_MOVEMENT_SETTINGS.setMovementMode(AuthoritativeMovementMode.CLIENT)
            SYNCED_PLAYER_MOVEMENT_SETTINGS.setRewindHistorySize(0)
            SYNCED_PLAYER_MOVEMENT_SETTINGS.setServerAuthoritativeBlockBreaking(false)
        }
    }
}