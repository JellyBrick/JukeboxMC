package org.jukeboxmc.player

import com.nukkitx.math.vector.Vector2f
import com.nukkitx.nbt.NbtMap
import com.nukkitx.network.util.DisconnectReason
import com.nukkitx.protocol.bedrock.BedrockPacket
import com.nukkitx.protocol.bedrock.BedrockServerSession
import com.nukkitx.protocol.bedrock.BedrockSession
import com.nukkitx.protocol.bedrock.data.AuthoritativeMovementMode
import com.nukkitx.protocol.bedrock.data.ChatRestrictionLevel
import com.nukkitx.protocol.bedrock.data.GamePublishSetting
import com.nukkitx.protocol.bedrock.data.PlayerPermission
import com.nukkitx.protocol.bedrock.data.SyncedPlayerMovementSettings
import com.nukkitx.protocol.bedrock.handler.BatchHandler
import com.nukkitx.protocol.bedrock.packet.AvailableEntityIdentifiersPacket
import com.nukkitx.protocol.bedrock.packet.BiomeDefinitionListPacket
import com.nukkitx.protocol.bedrock.packet.CraftingDataPacket
import com.nukkitx.protocol.bedrock.packet.CreativeContentPacket
import com.nukkitx.protocol.bedrock.packet.PlayStatusPacket
import com.nukkitx.protocol.bedrock.packet.PlayerListPacket
import com.nukkitx.protocol.bedrock.packet.SetEntityDataPacket
import com.nukkitx.protocol.bedrock.packet.SetTimePacket
import com.nukkitx.protocol.bedrock.packet.StartGamePacket
import com.nukkitx.protocol.bedrock.packet.UpdateAttributesPacket
import io.netty.buffer.ByteBuf
import org.jukeboxmc.Server
import org.jukeboxmc.crafting.CraftingManager
import org.jukeboxmc.event.network.PacketReceiveEvent
import org.jukeboxmc.event.network.PacketSendEvent
import org.jukeboxmc.event.player.PlayerQuitEvent
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.network.Network
import org.jukeboxmc.network.handler.HandlerRegistry
import org.jukeboxmc.network.handler.PacketHandler
import org.jukeboxmc.player.data.LoginData
import org.jukeboxmc.player.manager.PlayerChunkManager
import org.jukeboxmc.util.BiomeDefinitions
import org.jukeboxmc.util.CreativeItems
import org.jukeboxmc.util.EntityIdentifiers
import org.jukeboxmc.util.ItemPalette
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

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
            if (this.loginData == null && loginData !== null) {
                field = loginData
                player.name = loginData.displayName
                player.nameTag = loginData.displayName
                player.uuid = loginData.uuid
                player.skin = loginData.skin
                player.deviceInfo = loginData.deviceInfo
            }
        }
    val player: Player
    private var disconnectMessage: String? = null
    private val playerChunkManager: PlayerChunkManager

    init {
        this.session = session
        this.session.hardcodedBlockingId.set(Item.create<Item>(ItemType.SHIELD).runtimeId)
        this.session.addDisconnectHandler { disconnectReason: DisconnectReason? ->
            server.scheduler.execute {
                onDisconnect(
                    disconnectReason,
                )
            }
        }
        loggedIn = AtomicBoolean(false)
        spawned = AtomicBoolean(false)
        player = Player(server, this)
        playerChunkManager = PlayerChunkManager(player)
        session.packetCodec = Network.CODEC
        session.batchHandler =
            BatchHandler { bedrockSession: BedrockSession?, byteBuf: ByteBuf?, packets: Collection<BedrockPacket> ->
                packets.forEach { packet ->
                    server.scheduler.execute {
                        val packetReceiveEvent = PacketReceiveEvent(player, packet)
                        server.pluginManager.callEvent(packetReceiveEvent)
                        if (packetReceiveEvent.isCancelled) {
                            return@execute
                        }
                        val packetHandler =
                            HandlerRegistry.getPacketHandler<PacketHandler<BedrockPacket>>(packetReceiveEvent.packet::class.java)
                        if (packetHandler != null) {
                            packetHandler.handle(packetReceiveEvent.packet, server, player)
                        } else {
                            server.logger.info("Handler missing for packet: " + packet::class.java.simpleName)
                        }
                    }
                }
            }
    }

    fun update() {
        if (isClosed || !loggedIn.get()) {
            return
        }
        if (spawned.get()) {
            playerChunkManager.queueNewChunks()
        }
        playerChunkManager.sendQueued()
        if (playerChunkManager.chunksSent >= 25 && !spawned.get() && player.teleportLocation == null) {
            doFirstSpawn()
        }
    }

    private fun onDisconnect(disconnectReason: DisconnectReason?) {
        server.removePlayer(player)
        player.world?.removeEntity(player)
        player.chunk?.removeEntity(player)
        player.inventory.removeViewer(player)
        player.armorInventory.removeViewer(player)
        player.currentInventory?.removeViewer(player)
        player.getCreativeItemCacheInventory().removeViewer(player)
        player.getCraftingGridInventory().removeViewer(player)
        player.getCraftingTableInventory().removeViewer(player)
        player.getCartographyTableInventory().removeViewer(player)
        player.getSmithingTableInventory().removeViewer(player)
        player.getAnvilInventory().removeViewer(player)
        // this.player.getEnderChestInventory().removeViewer( this.player );
        player.getStoneCutterInventory().removeViewer(player)
        player.getCraftingGridInventory().removeViewer(player)
        player.getOffHandInventory().removeViewer(player)
        server.removeFromTabList(player)
        playerChunkManager.clear()
        player.close()
        val playerQuitEvent = PlayerQuitEvent(player, "§e" + player.name + " left the game")
        Server.instance.pluginManager.callEvent(playerQuitEvent)
        if (playerQuitEvent.quitMessage.isNotEmpty()) {
            server.broadcastMessage(playerQuitEvent.quitMessage)
        }
        server.logger.info(
            player.name + " logged out reason: " + if (disconnectMessage == null) {
                parseDisconnectMessage(
                    disconnectReason,
                )
            } else {
                disconnectMessage
            },
        )
    }

    private fun doFirstSpawn() {
        spawned.set(true)
        player.world!!.addEntity(player)
        val setEntityDataPacket = SetEntityDataPacket()
        setEntityDataPacket.runtimeEntityId = player.entityId
        setEntityDataPacket.metadata.putAll(player.metadata.getEntityDataMap())
        setEntityDataPacket.tick = server.currentTick
        sendPacket(setEntityDataPacket)
        val adventureSettings = player.adventureSettings
        if (server.isOperatorInFile(player.name)) {
            adventureSettings[AdventureSettings.Type.OPERATOR] = true
        }
        adventureSettings[AdventureSettings.Type.WORLD_IMMUTABLE] = player.gameMode!!.ordinal == 3
        adventureSettings[AdventureSettings.Type.ALLOW_FLIGHT] = player.gameMode!!.ordinal > 0
        adventureSettings[AdventureSettings.Type.NO_CLIP] = player.gameMode!!.ordinal == 3
        adventureSettings[AdventureSettings.Type.FLYING] = player.gameMode!!.ordinal == 3
        adventureSettings[AdventureSettings.Type.ATTACK_MOBS] = player.gameMode!!.ordinal < 2
        adventureSettings[AdventureSettings.Type.ATTACK_PLAYERS] = player.gameMode!!.ordinal < 2
        adventureSettings[AdventureSettings.Type.NO_PVM] = player.gameMode!!.ordinal == 3
        adventureSettings.update()
        player.sendCommandData()
        val updateAttributesPacket = UpdateAttributesPacket()
        updateAttributesPacket.runtimeEntityId = player.entityId
        for (attribute in player.getAttributes()) {
            updateAttributesPacket.attributes.add(attribute.toNetwork())
        }
        updateAttributesPacket.tick = server.currentTick
        sendPacket(updateAttributesPacket)
        server.addToTabList(player)
        if (server.onlinePlayers.size > 1) {
            val playerListPacket = PlayerListPacket()
            playerListPacket.action = PlayerListPacket.Action.ADD
            server.getPlayerListEntry().forEach { (uuid: UUID, entry: PlayerListPacket.Entry?) ->
                if (uuid !== player.uuid) {
                    playerListPacket.entries.add(entry)
                }
            }
            player.playerConnection.sendPacket(playerListPacket)
        }
        player.inventory.addViewer(player)
        player.inventory.sendContents(player)
        player.getCursorInventory().addViewer(player)
        player.getCursorInventory().sendContents(player)
        player.armorInventory.addViewer(player)
        player.armorInventory.sendContents(player)
        val playStatusPacket = PlayStatusPacket()
        playStatusPacket.status = PlayStatusPacket.Status.PLAYER_SPAWN
        sendPacket(playStatusPacket)
        val setTimePacket = SetTimePacket()
        setTimePacket.time = player.world!!.getWorldTime()
        sendPacket(setTimePacket)
        for (onlinePlayer in server.onlinePlayers) {
            if (onlinePlayer.dimension == player.dimension) {
                player.spawn(onlinePlayer)
                onlinePlayer.spawn(player)
            }
        }
        server.logger.info(
            player.name + " logged in [World=" + player.world!!.name + ", X=" +
                player.blockX + ", Y=" + player.blockY + ", Z=" + player.blockZ +
                ", Dimension=" + player.location.dimension.dimensionName + "]",
        )
    }

    fun initializePlayer() {
        loggedIn.set(true)
        val startGamePacket = StartGamePacket()
        startGamePacket.serverEngine = "JukeboxMC"
        startGamePacket.uniqueEntityId = player.entityId
        startGamePacket.runtimeEntityId = player.entityId
        startGamePacket.playerGameType = player.gameMode!!.toGameType()
        startGamePacket.playerPosition = player.location.toVector3f().add(0f, 2f, 0f) // TODO
        startGamePacket.defaultSpawn = player.location.toVector3i().add(0, 2, 0) // TODO
        startGamePacket.rotation = Vector2f.from(player.pitch, player.yaw)
        startGamePacket.seed = 0L // TODO
        startGamePacket.dimensionId = player.location.dimension.ordinal
        startGamePacket.isTrustingPlayers = true
        startGamePacket.levelGameType = server.gameMode.toGameType()
        startGamePacket.difficulty = player.world!!.difficulty.ordinal
        startGamePacket.isAchievementsDisabled = true
        startGamePacket.dayCycleStopTime = 0
        startGamePacket.rainLevel = 0f
        startGamePacket.lightningLevel = 0f
        startGamePacket.isCommandsEnabled = true
        startGamePacket.isMultiplayerGame = true
        startGamePacket.isBroadcastingToLan = true
        startGamePacket.gamerules.addAll(player.world!!.getGameRules().getGameRules())
        startGamePacket.levelId = ""
        startGamePacket.levelName = player.world!!.name
        startGamePacket.generatorId = 1
        startGamePacket.itemEntries = ItemPalette.entries
        startGamePacket.xblBroadcastMode = GamePublishSetting.PUBLIC
        startGamePacket.platformBroadcastMode = GamePublishSetting.PUBLIC
        startGamePacket.defaultPlayerPermission = PlayerPermission.MEMBER
        startGamePacket.serverChunkTickRange = 4
        startGamePacket.vanillaVersion = Network.CODEC.getMinecraftVersion()
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
        availableEntityIdentifiersPacket.identifiers = EntityIdentifiers.identifiers
        sendPacket(availableEntityIdentifiersPacket)
        val biomeDefinitionListPacket = BiomeDefinitionListPacket()
        biomeDefinitionListPacket.definitions = BiomeDefinitions.biomeDefinitions
        sendPacket(biomeDefinitionListPacket)
        val creativeContentPacket = CreativeContentPacket()
        creativeContentPacket.contents = CreativeItems.creativeItems.toTypedArray()
        sendPacket(creativeContentPacket)
        val craftingManager: CraftingManager = server.getCraftingManager()
        val craftingDataPacket = CraftingDataPacket()
        craftingDataPacket.craftingData.addAll(craftingDataPacket.craftingData)
        craftingDataPacket.potionMixData.addAll(craftingManager.potionMixData)
        craftingDataPacket.containerMixData.addAll(craftingManager.containerMixData)
        craftingDataPacket.isCleanRecipes = true
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

    fun sendPlayStatus(status: PlayStatusPacket.Status) {
        val playStatusPacket = PlayStatusPacket()
        playStatusPacket.status = status
        sendPacketImmediately(playStatusPacket)
    }

    fun sendPacket(packet: BedrockPacket) {
        if (!isClosed && session.packetCodec != null) {
            val packetSendEvent = PacketSendEvent(player, packet)
            Server.instance.pluginManager.callEvent(packetSendEvent)
            if (packetSendEvent.isCancelled) {
                return
            }
            session.sendPacket(packetSendEvent.packet)
        }
    }

    fun sendPacketImmediately(packet: BedrockPacket) {
        if (!isClosed) {
            session.sendPacketImmediately(packet)
        }
    }

    val isClosed: Boolean
        get() = session.isClosed

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
            SYNCED_PLAYER_MOVEMENT_SETTINGS.movementMode = AuthoritativeMovementMode.CLIENT
            SYNCED_PLAYER_MOVEMENT_SETTINGS.rewindHistorySize = 0
            SYNCED_PLAYER_MOVEMENT_SETTINGS.isServerAuthoritativeBlockBreaking = false
        }
    }
}
