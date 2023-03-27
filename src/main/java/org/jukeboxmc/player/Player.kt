package org.jukeboxmc.player

import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import org.cloudburstmc.math.vector.Vector3f
import org.cloudburstmc.protocol.bedrock.data.command.CommandData
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes
import org.cloudburstmc.protocol.bedrock.data.entity.EntityEventType
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag
import org.cloudburstmc.protocol.bedrock.packet.AvailableCommandsPacket
import org.cloudburstmc.protocol.bedrock.packet.ChangeDimensionPacket
import org.cloudburstmc.protocol.bedrock.packet.ContainerClosePacket
import org.cloudburstmc.protocol.bedrock.packet.DeathInfoPacket
import org.cloudburstmc.protocol.bedrock.packet.EntityEventPacket
import org.cloudburstmc.protocol.bedrock.packet.ModalFormRequestPacket
import org.cloudburstmc.protocol.bedrock.packet.MovePlayerPacket
import org.cloudburstmc.protocol.bedrock.packet.PlaySoundPacket
import org.cloudburstmc.protocol.bedrock.packet.RespawnPacket
import org.cloudburstmc.protocol.bedrock.packet.ServerSettingsResponsePacket
import org.cloudburstmc.protocol.bedrock.packet.SetPlayerGameTypePacket
import org.cloudburstmc.protocol.bedrock.packet.SetSpawnPositionPacket
import org.cloudburstmc.protocol.bedrock.packet.TextPacket
import org.cloudburstmc.protocol.bedrock.packet.ToastRequestPacket
import org.cloudburstmc.protocol.bedrock.packet.UpdateAttributesPacket
import org.jukeboxmc.Server
import org.jukeboxmc.command.CommandSender
import org.jukeboxmc.entity.Entity
import org.jukeboxmc.entity.EntityLiving
import org.jukeboxmc.entity.EntityMoveable
import org.jukeboxmc.entity.attribute.AttributeType
import org.jukeboxmc.entity.passiv.EntityHuman
import org.jukeboxmc.entity.projectile.EntityFishingHook
import org.jukeboxmc.event.entity.EntityDamageByEntityEvent
import org.jukeboxmc.event.entity.EntityDamageEvent
import org.jukeboxmc.event.entity.EntityDamageEvent.DamageSource
import org.jukeboxmc.event.entity.EntityHealEvent
import org.jukeboxmc.event.inventory.InventoryCloseEvent
import org.jukeboxmc.event.inventory.InventoryOpenEvent
import org.jukeboxmc.event.player.PlayerChangeSkinEvent
import org.jukeboxmc.event.player.PlayerDeathEvent
import org.jukeboxmc.event.player.PlayerExperienceChangeEvent
import org.jukeboxmc.event.player.PlayerKickEvent
import org.jukeboxmc.event.player.PlayerRespawnEvent
import org.jukeboxmc.form.Form
import org.jukeboxmc.form.FormListener
import org.jukeboxmc.form.NpcDialogueForm
import org.jukeboxmc.inventory.AnvilInventory
import org.jukeboxmc.inventory.CartographyTableInventory
import org.jukeboxmc.inventory.ContainerInventory
import org.jukeboxmc.inventory.CraftingGridInventory
import org.jukeboxmc.inventory.CraftingTableInventory
import org.jukeboxmc.inventory.CreativeItemCacheInventory
import org.jukeboxmc.inventory.CursorInventory
import org.jukeboxmc.inventory.EnderChestInventory
import org.jukeboxmc.inventory.GrindstoneInventory
import org.jukeboxmc.inventory.InventoryHolder
import org.jukeboxmc.inventory.OffHandInventory
import org.jukeboxmc.inventory.SmallCraftingGridInventory
import org.jukeboxmc.inventory.SmithingTableInventory
import org.jukeboxmc.inventory.StoneCutterInventory
import org.jukeboxmc.inventory.WindowId
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemArmor
import org.jukeboxmc.item.enchantment.EnchantmentKnockback
import org.jukeboxmc.item.enchantment.EnchantmentSharpness
import org.jukeboxmc.item.enchantment.EnchantmentType
import org.jukeboxmc.math.Location
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.data.LoginData
import org.jukeboxmc.player.skin.Skin
import org.jukeboxmc.potion.Effect
import org.jukeboxmc.potion.EffectType
import org.jukeboxmc.util.Utils
import org.jukeboxmc.world.Difficulty
import org.jukeboxmc.world.Sound
import org.jukeboxmc.world.chunk.ChunkLoader
import java.util.Locale
import java.util.Objects
import java.util.UUID
import kotlin.math.cos
import kotlin.math.sin

/**
 * @author LucGamesYT
 * @version 1.0
 */
open class Player(
    override val server: Server,
    val playerConnection: PlayerConnection,
) : EntityHuman(), ChunkLoader, CommandSender, InventoryHolder {
    val adventureSettings: AdventureSettings = AdventureSettings(this)
    override var name: String = ""
        set(value) {
            if (!playerConnection.isLoggedIn()) {
                field = value
            }
        }
    var gameMode: GameMode = playerConnection.server.gameMode
        set(gameMode) {
            field = gameMode
            adventureSettings[AdventureSettings.Type.WORLD_IMMUTABLE] = field.ordinal == 3
            adventureSettings[AdventureSettings.Type.ALLOW_FLIGHT] = field.ordinal > 0
            adventureSettings[AdventureSettings.Type.NO_CLIP] = field.ordinal == 3
            adventureSettings[AdventureSettings.Type.FLYING] = field.ordinal == 3
            adventureSettings[AdventureSettings.Type.ATTACK_MOBS] = field.ordinal < 2
            adventureSettings[AdventureSettings.Type.ATTACK_PLAYERS] = field.ordinal < 2
            adventureSettings[AdventureSettings.Type.NO_PVM] = field.ordinal == 3
            adventureSettings.update()
            val setPlayerGameTypePacket = SetPlayerGameTypePacket()
            setPlayerGameTypePacket.gamemode = gameMode.id
            playerConnection.sendPacket(setPlayerGameTypePacket)
        }
    var closingWindowId = Int.MIN_VALUE
    var currentInventory: ContainerInventory? = null
    private var craftingGridInventory: CraftingGridInventory
    private val creativeItemCacheInventory: CreativeItemCacheInventory = CreativeItemCacheInventory(this)
    private val cursorInventory: CursorInventory
    private val craftingTableInventory: CraftingTableInventory
    private val cartographyTableInventory: CartographyTableInventory
    private val smithingTableInventory: SmithingTableInventory
    private val anvilInventory: AnvilInventory
    private val enderChestInventory: EnderChestInventory
    private val stoneCutterInventory: StoneCutterInventory
    private val grindstoneInventory: GrindstoneInventory
    private val offHandInventory: OffHandInventory
    var inAirTicks = 0
    var highestPosition = 0f
    var lastBreakTime: Long = 0
    var lasBreakPosition: Vector
    var isBreakingBlock = false
    var teleportLocation: Location? = null
        private set
    var spawnLocation: Location = location.world.spawnLocation
        set(value) {
            field = value
            val setSpawnPositionPacket = SetSpawnPositionPacket()
            setSpawnPositionPacket.spawnType = SetSpawnPositionPacket.Type.PLAYER_SPAWN
            setSpawnPositionPacket.dimensionId = value.dimension.ordinal
            setSpawnPositionPacket.spawnPosition = value.toVector3i()
            setSpawnPositionPacket.blockPosition = location.world.spawnLocation.toVector3i()
            playerConnection.sendPacket(setSpawnPositionPacket)
        }
    var respawnLocation: Location? = null
    private val permissions: MutableMap<UUID?, MutableSet<String>> = HashMap()
    var entityFishingHook: EntityFishingHook? = null
    private var hasOpenForm = false
    private var formId = 0
    private var serverSettingsForm = -1
    private val forms: Int2ObjectMap<Form<*>> = Int2ObjectOpenHashMap()
    private val formListeners: Int2ObjectMap<FormListener<*>> = Int2ObjectOpenHashMap()
    private val npcDialogueForms = ObjectArrayList<NpcDialogueForm?>()

    init {
        craftingGridInventory = SmallCraftingGridInventory(this)
        cursorInventory = CursorInventory(this)
        craftingTableInventory = CraftingTableInventory(this)
        cartographyTableInventory = CartographyTableInventory(this)
        smithingTableInventory = SmithingTableInventory(this)
        anvilInventory = AnvilInventory(this)
        enderChestInventory = EnderChestInventory(this)
        stoneCutterInventory = StoneCutterInventory(this)
        grindstoneInventory = GrindstoneInventory(this)
        offHandInventory = OffHandInventory(this)
        lasBreakPosition = Vector(0, 0, 0)
    }

    override fun update(currentTick: Long) {
        if (!playerConnection.isLoggedIn()) {
            return
        }
        super.update(currentTick)
        val nearbyEntities = this.world.getNearbyEntities(boundingBox.grow(1f, 0.5f, 1f), location.dimension, null)
        for (nearbyEntity in nearbyEntities) {
            (nearbyEntity as? EntityMoveable)?.onCollideWithPlayer(this)
        }
        if (!isOnGround && !this.isOnLadder) {
            ++inAirTicks
            if (inAirTicks > 5) {
                if (location.y > highestPosition) {
                    highestPosition = location.y
                }
            }
        } else {
            if (inAirTicks > 0) {
                inAirTicks = 0
            }
            fallDistance = highestPosition - location.y
            if (fallDistance > 0) {
                fall()
                highestPosition = location.y
                fallDistance = 0f
            }
        }
        if (!isDead) {
            val hungerAttribute = getAttribute(AttributeType.PLAYER_HUNGER)
            val hunger = hungerAttribute.getCurrentValue()
            var health = -1f
            val difficulty: Difficulty = this.world.difficulty
            if (difficulty == Difficulty.PEACEFUL && foodTicks % 10 == 0) {
                if (hunger < hungerAttribute.maxValue) {
                    addHunger(1)
                }
                if (foodTicks % 20 == 0) {
                    health = this.health
                    if (health < getAttribute(AttributeType.HEALTH).maxValue) {
                        this.setHeal(1f, EntityHealEvent.Cause.SATURATION)
                    }
                }
            }
            if (foodTicks == 0) {
                if (hunger >= 18) {
                    if (health == -1f) {
                        health = this.health
                    }
                    if (health < 20) {
                        this.setHeal(1f, EntityHealEvent.Cause.SATURATION)
                        if (gameMode == GameMode.SURVIVAL) {
                            exhaust(3f)
                        }
                    }
                } else if (hunger <= 0) {
                    if (health == -1f) {
                        health = this.health
                    }
                    if (difficulty == Difficulty.NORMAL && health > 2 || difficulty == Difficulty.HARD && health > 1) {
                        damage(EntityDamageEvent(this, 1f, DamageSource.STARVE))
                    }
                }
            }
            foodTicks++
            if (foodTicks >= 80) {
                foodTicks = 0
            }
            if (hasEffect(EffectType.HUNGER)) {
                exhaust(0.1f * (getEffect<Effect>(EffectType.HUNGER)!!.amplifier + 1))
            }
        }
        val breathing = !this.isInWater || hasEffect(EffectType.WATER_BREATHING)
        if (metadata.getFlag(EntityFlag.BREATHING) != breathing && gameMode == GameMode.SURVIVAL) {
            this.updateMetadata(metadata.setFlag(EntityFlag.BREATHING, breathing))
        }
        var air = metadata.getShort(EntityDataTypes.AIR_SUPPLY)!!
        val maxAir = metadata.getShort(EntityDataTypes.AIR_SUPPLY_MAX)!!
        if (gameMode == GameMode.SURVIVAL) {
            if (!breathing) {
                if (--air < 0) {
                    if (currentTick % 20 == 0L) {
                        damage(EntityDamageEvent(this, 2f, DamageSource.DROWNING))
                    }
                } else {
                    this.updateMetadata(metadata.setShort(EntityDataTypes.AIR_SUPPLY, air))
                }
            } else {
                if (air != maxAir) {
                    this.updateMetadata(metadata.setShort(EntityDataTypes.AIR_SUPPLY, maxAir))
                }
            }
        }
        checkTeleportPosition()
        updateAttributes()
    }

    val isSpawned: Boolean
        get() = playerConnection.isSpawned()
    val isLoggedIn: Boolean
        get() = playerConnection.isLoggedIn()

    val xuid: String
        get() = playerConnection.loginData?.xuid ?: ""

    override fun sendMessage(message: String) {
        val textPacket = TextPacket()
        textPacket.type = TextPacket.Type.RAW
        textPacket.message = message
        textPacket.isNeedsTranslation = false
        textPacket.xuid = xuid
        textPacket.platformChatId = deviceInfo.deviceId
        playerConnection.sendPacket(textPacket)
    }

    fun sendTip(text: String) {
        val textPacket = TextPacket()
        textPacket.type = TextPacket.Type.TIP
        textPacket.message = text
        textPacket.isNeedsTranslation = false
        textPacket.xuid = playerConnection.loginData?.xuid
        textPacket.platformChatId = deviceInfo.deviceId
        playerConnection.sendPacket(textPacket)
    }

    fun sendPopup(text: String) {
        val textPacket = TextPacket()
        textPacket.type = TextPacket.Type.POPUP
        textPacket.message = text
        textPacket.isNeedsTranslation = false
        textPacket.xuid = playerConnection.loginData?.xuid
        textPacket.platformChatId = deviceInfo.deviceId
        playerConnection.sendPacket(textPacket)
    }

    override fun hasPermission(permission: String): Boolean {
        return permissions.containsKey(uuid) && permissions[uuid]!!.contains(permission.lowercase(Locale.getDefault())) || isOp || permission.isEmpty()
    }

    fun addPermission(permission: String) {
        if (!permissions.containsKey(uuid)) {
            permissions[uuid] = HashSet()
        }
        permissions[uuid]!!.add(permission.lowercase(Locale.getDefault()))
        sendCommandData()
    }

    fun addPermissions(permissions: Collection<String>) {
        if (!this.permissions.containsKey(uuid)) {
            this.permissions[uuid] = HashSet(permissions)
        } else {
            this.permissions[uuid]!!.addAll(permissions)
        }
        sendCommandData()
    }

    fun removePermission(permission: String) {
        if (permissions.containsKey(uuid)) {
            permissions[uuid]!!.remove(permission)
        }
        sendCommandData()
    }

    fun removePermissions(permissions: Collection<String>) {
        if (this.permissions.containsKey(uuid)) {
            this.permissions[uuid]!!.removeAll(permissions.toSet())
        }
        sendCommandData()
    }

    var isOp: Boolean
        get() = adventureSettings[AdventureSettings.Type.OPERATOR]
        set(value) {
            adventureSettings[AdventureSettings.Type.OPERATOR] = value
            adventureSettings.update()
            if (value) {
                server.addOperatorToFile(name)
            } else {
                server.removeOperatorFromFile(name)
            }
            sendCommandData()
        }
    var chunkRadius: Int
        get() = playerConnection.getPlayerChunkManager().chunkRadius
        set(chunkRadius) {
            playerConnection.getPlayerChunkManager().chunkRadius = chunkRadius
        }

    fun isChunkLoaded(chunkX: Int, chunkZ: Int): Boolean {
        return playerConnection.getPlayerChunkManager().isChunkInView(chunkX, chunkZ)
    }

    fun resendChunk(chunkX: Int, chunkZ: Int) {
        playerConnection.getPlayerChunkManager().resendChunk(chunkX, chunkZ)
    }

    fun getCraftingGridInventory(): CraftingGridInventory {
        return craftingGridInventory
    }

    fun setCraftingGridInventory(craftingGridInventory: CraftingGridInventory) {
        this.craftingGridInventory = craftingGridInventory
    }

    fun getCreativeItemCacheInventory(): CreativeItemCacheInventory {
        return creativeItemCacheInventory
    }

    fun getCursorInventory(): CursorInventory {
        return cursorInventory
    }

    fun getCraftingTableInventory(): CraftingTableInventory {
        return craftingTableInventory
    }

    fun getCartographyTableInventory(): CartographyTableInventory {
        return cartographyTableInventory
    }

    fun getSmithingTableInventory(): SmithingTableInventory {
        return smithingTableInventory
    }

    fun getAnvilInventory(): AnvilInventory {
        return anvilInventory
    }

    fun getEnderChestInventory(): EnderChestInventory {
        return enderChestInventory
    }

    fun getStoneCutterInventory(): StoneCutterInventory {
        return stoneCutterInventory
    }

    fun getGrindstoneInventory(): GrindstoneInventory {
        return grindstoneInventory
    }

    fun getOffHandInventory(): OffHandInventory {
        return offHandInventory
    }

    @JvmOverloads
    fun openInventory(
        inventory: ContainerInventory,
        position: Vector = location,
        windowId: Byte = WindowId.OPEN_CONTAINER.id.toByte(),
    ) {
        if (currentInventory != null) {
            return
        }
        val inventoryOpenEvent = InventoryOpenEvent(inventory, this)
        Server.instance.pluginManager.callEvent(inventoryOpenEvent)
        if (inventoryOpenEvent.isCancelled) {
            return
        }
        inventory.addViewer(this, position, windowId)
        currentInventory = inventory
    }

    fun closeInventory(inventory: ContainerInventory) {
        if (currentInventory === inventory) {
            this.closeInventory(WindowId.OPEN_CONTAINER.id.toByte())
        }
    }

    fun closeInventory(windowId: Byte) {
        if (currentInventory != null) {
            val containerClosePacket = ContainerClosePacket()
            containerClosePacket.id = windowId
            containerClosePacket.isServerInitiated = closingWindowId != windowId.toInt()
            playerConnection.sendPacket(containerClosePacket)
            currentInventory!!.removeViewer(this)
            Server.instance.pluginManager.callEvent(
                InventoryCloseEvent(
                    currentInventory!!,
                    this,
                ),
            )
            currentInventory = null
        }
    }

    fun closeInventory(windowId: Byte, isServerSide: Boolean) {
        if (currentInventory != null) {
            val containerClosePacket = ContainerClosePacket()
            containerClosePacket.id = windowId
            containerClosePacket.isServerInitiated = isServerSide
            playerConnection.sendPacket(containerClosePacket)
            currentInventory!!.removeViewer(this)
            Server.instance.pluginManager.callEvent(
                InventoryCloseEvent(
                    currentInventory!!,
                    this,
                ),
            )
            currentInventory = null
        } else {
            val containerClosePacket = ContainerClosePacket()
            containerClosePacket.id = windowId
            containerClosePacket.isServerInitiated = isServerSide
            playerConnection.sendPacket(containerClosePacket)
        }
    }

    protected fun checkTeleportPosition() {
        if (teleportLocation != null) {
            val chunkX = teleportLocation!!.chunkX
            val chunkZ = teleportLocation!!.chunkZ
            for (X in -1..1) {
                for (Z in -1..1) {
                    val index = Utils.toLong(chunkX + X, chunkZ + Z)
                    if (!playerConnection.getPlayerChunkManager().isChunkInView(index)) {
                        return
                    }
                }
            }
            teleportLocation = null
        }
    }

    fun teleport(player: Player) {
        this.teleport(player.location)
    }

    fun teleport(location: Location) {
        val currentWorld = this.world
        val world = location.world
        val fromDimension = this.location.dimension
        teleportLocation = location
        playerConnection.getPlayerChunkManager().queueNewChunks(teleportLocation!!)

        // TODO DESPAWN PLAYER AND SPAWN TO NEW DIMENSION
        if (fromDimension != location.dimension) {
            playerConnection.getPlayerChunkManager().clear()
            val setSpawnPositionPacket = SetSpawnPositionPacket()
            setSpawnPositionPacket.dimensionId = location.dimension.ordinal
            setSpawnPositionPacket.spawnPosition = location.toVector3i().div(8, 8, 8)
            setSpawnPositionPacket.spawnType = SetSpawnPositionPacket.Type.PLAYER_SPAWN
            playerConnection.sendPacket(setSpawnPositionPacket)
            val changeDimensionPacket = ChangeDimensionPacket()
            changeDimensionPacket.position = this.location.toVector3f()
            changeDimensionPacket.dimension = location.dimension.ordinal
            changeDimensionPacket.isRespawn = false
            playerConnection.sendPacket(changeDimensionPacket)
        } else if (currentWorld.name != world.name) {
            playerConnection.getPlayerChunkManager().clear()
            this.despawn()
            currentWorld.players?.forEach { player: Player? -> player!!.despawn(this) }
            val loadedChunk = this.loadedChunk
            if (loadedChunk != null) {
                loadedChunk.removeEntity(this)
            } else {
                server.logger.info("Loaded chunk for removeEntity is null")
            }
            currentWorld.removeEntity(this)
            this.location = location
            world.addEntity(this)
            if (loadedChunk != null) {
                loadedChunk.addEntity(this)
            } else {
                server.logger.info("Loaded chunk for addEntity is null")
            }
            this.spawn()
            world.players.forEach { player -> player.spawn(this) }
        }
        move(location, MovePlayerPacket.Mode.TELEPORT)
        checkTeleportPosition()
    }

    private fun move(location: Location, mode: MovePlayerPacket.Mode) {
        val movePlayerPacket = MovePlayerPacket()
        movePlayerPacket.runtimeEntityId = entityId
        movePlayerPacket.position = location.toVector3f().add(0f, this.eyeHeight, 0f)
        movePlayerPacket.rotation = Vector3f.from(location.pitch, location.yaw, location.yaw)
        movePlayerPacket.mode = mode
        if (mode == MovePlayerPacket.Mode.TELEPORT) {
            movePlayerPacket.teleportationCause = MovePlayerPacket.TeleportationCause.BEHAVIOR
        }
        movePlayerPacket.isOnGround = isOnGround
        movePlayerPacket.ridingRuntimeEntityId = 0
        movePlayerPacket.tick = server.currentTick
        playerConnection.sendPacket(movePlayerPacket)
    }

    @JvmOverloads
    fun kick(reason: String, hideScreen: Boolean = false) {
        val playerKickEvent = PlayerKickEvent(this, reason)
        Server.instance.pluginManager.callEvent(playerKickEvent)
        if (playerKickEvent.isCancelled) {
            return
        }
        close()
        playerConnection.disconnect(playerKickEvent.reason, hideScreen)
    }

    fun sendToast(title: String, content: String) {
        val toastRequestPacket = ToastRequestPacket()
        toastRequestPacket.title = title
        toastRequestPacket.content = content
        playerConnection.sendPacket(toastRequestPacket)
    }

    fun sendCommandData() {
        val availableCommandsPacket = AvailableCommandsPacket()
        val commandList: MutableSet<CommandData> = HashSet()
        for (command in server.pluginManager.getCommandManager().getCommands()) {
            if (!hasPermission(command.commandData.getPermission())) {
                continue
            }
            commandList.add(command.commandData.toNetwork())
        }
        availableCommandsPacket.commands.addAll(commandList)
        playerConnection.sendPacket(availableCommandsPacket)
    }

    fun playSound(sound: Sound) {
        this.playSound(location, sound, 1f, 1f, false)
    }

    fun playSound(sound: Sound, volume: Float, pitch: Float) {
        this.playSound(location, sound, volume, pitch, false)
    }

    @JvmOverloads
    fun playSound(position: Vector, sound: Sound, volume: Float = 1f, pitch: Float = 1f, sendInChunk: Boolean = false) {
        val playSoundPacket = PlaySoundPacket()
        playSoundPacket.position = position.toVector3f()
        playSoundPacket.sound = sound.sound
        playSoundPacket.volume = volume
        playSoundPacket.pitch = pitch
        if (sendInChunk) {
            this.world.sendChunkPacket(position.chunkX, position.chunkZ, playSoundPacket)
        } else {
            playerConnection.sendPacket(playSoundPacket)
        }
    }

    fun updateAttributes() {
        var updateAttributesPacket: UpdateAttributesPacket? = null
        for (attribute in getAttributes()) {
            if (attribute.isDirty()) {
                if (updateAttributesPacket == null) {
                    updateAttributesPacket = UpdateAttributesPacket()
                    updateAttributesPacket.runtimeEntityId = entityId
                }
                updateAttributesPacket.attributes.add(attribute.toNetwork())
            }
        }
        if (updateAttributesPacket != null) {
            updateAttributesPacket.tick = server.currentTick
            playerConnection.sendPacket(updateAttributesPacket)
        }
    }

    var flySpeed: Float
        get() = adventureSettings.flySpeed
        set(value) {
            adventureSettings.flySpeed = value
            adventureSettings.update()
        }
    var walkSpeed: Float
        get() = adventureSettings.walkSpeed
        set(value) {
            adventureSettings.walkSpeed = value
            adventureSettings.update()
        }
    override var skin: Skin = Skin()
        set(skin) {
            field = skin
            val playerChangeSkinEvent = PlayerChangeSkinEvent(this, skin)
            if (playerChangeSkinEvent.isCancelled) return
            Server.instance.addToTabList(uuid, entityId, name, deviceInfo, xuid, skin)
        }

    fun sendServerSettings(player: Player) {
        if (serverSettingsForm != -1) {
            val form = forms[serverSettingsForm]
            val response = ServerSettingsResponsePacket()
            response.formId = serverSettingsForm
            response.formData = Utils.jackson.writeValueAsString(form)
            player.playerConnection.sendPacket(response)
        }
    }

    fun <R> showForm(form: Form<R>): FormListener<R> {
        if (hasOpenForm) {
            return FormListener()
        }
        val formId = formId++
        forms.put(formId, form)
        val formListener = FormListener<R>()
        formListeners.put(formId, formListener)
        val packetModalRequest = ModalFormRequestPacket()
        packetModalRequest.formId = formId
        packetModalRequest.formData = Utils.jackson.writeValueAsString(form)
        playerConnection.sendPacket(packetModalRequest)
        hasOpenForm = true

        // Dirty fix to show image on buttonlist
        Server.instance.scheduler
            .scheduleDelayed({ setAttributes(AttributeType.PLAYER_LEVEL, level) }, 5)
        return formListener
    }

    fun <R> setSettingsForm(form: Form<R>): FormListener<R> {
        if (serverSettingsForm != -1) {
            removeSettingsForm()
        }
        val formId = formId++
        forms.put(formId, form)
        val formListener = FormListener<R>()
        formListeners.put(formId, formListener)
        serverSettingsForm = formId
        return formListener
    }

    fun removeSettingsForm() {
        if (serverSettingsForm != -1) {
            forms.remove(serverSettingsForm)
            formListeners.remove(serverSettingsForm)
            serverSettingsForm = -1
        }
    }

    fun parseGUIResponse(formId: Int, json: String?) {
        // Get the listener and the form
        val form = forms[formId]
        if (form != null) {
            // Get listener
            val formListener = formListeners.getValue(formId) as FormListener<Any>
            if (serverSettingsForm != formId) {
                forms.remove(formId)
                formListeners.remove(formId)
            }
            hasOpenForm = false
            if (json == null || json == "null") {
                formListener.close()
            } else {
                val resp = form.parseResponse(json)
                if (resp == null) {
                    formListener.close()
                } else {
                    formListener.response(resp)
                }
            }
        }
    }

    fun addNpcDialogueForm(npcDialogueForm: NpcDialogueForm?) {
        npcDialogueForms.add(npcDialogueForm)
    }

    fun removeNpcDialogueForm(npcDialogueForm: NpcDialogueForm?) {
        npcDialogueForms.remove(npcDialogueForm)
    }

    val openNpcDialogueForms: Set<NpcDialogueForm?>
        get() = HashSet(npcDialogueForms)
    override var experience: Float
        get() = super.experience
        set(value) {
            val playerExperienceChangeEvent =
                PlayerExperienceChangeEvent(this, experience.toInt(), level.toInt(), value.toInt(), 0)
            if (playerExperienceChangeEvent.isCancelled) {
                return
            }
            super.experience = playerExperienceChangeEvent.newExperience.toFloat()
        }
    override var level: Float
        get() = super.level
        set(value) {
            val playerExperienceChangeEvent =
                PlayerExperienceChangeEvent(this, experience.toInt(), level.toInt(), 0, value.toInt())
            if (playerExperienceChangeEvent.isCancelled) {
                return
            }
            super.level = playerExperienceChangeEvent.newLevel.toFloat()
        }
    val loginData: LoginData?
        get() = playerConnection.loginData

    // =========== Damage ===========
    override fun damage(event: EntityDamageEvent): Boolean {
        return if (adventureSettings[AdventureSettings.Type.ALLOW_FLIGHT] && event.damageSource == DamageSource.FALL) {
            false
        } else {
            gameMode != GameMode.CREATIVE && gameMode != GameMode.SPECTATOR && super.damage(event)
        }
    }

    override fun applyArmorReduction(event: EntityDamageEvent, damageArmor: Boolean): Float {
        if (event.damageSource == DamageSource.FALL || event.damageSource == DamageSource.VOID || event.damageSource == DamageSource.DROWNING) {
            return event.damage
        }
        val damage = event.damage
        val totalArmorValue = armorInventory.totalArmorValue
        if (damageArmor) {
            armorInventory.damageEvenly(damage)
        }
        return -damage * totalArmorValue * 0.04f
    }

    override fun applyFeatherFallingReduction(event: EntityDamageEvent, damage: Float): Float {
        if (event.damageSource == DamageSource.FALL) {
            val boots = armorInventory.boots
            if (boots != null && boots.type != ItemType.AIR) {
                val enchantment = boots.getEnchantment(EnchantmentType.FEATHER_FALLING)
                if (enchantment != null) {
                    val featherFallingReduction = 12 * enchantment.level
                    return -(damage * (featherFallingReduction / 100f))
                }
            }
        }
        return event.damage
    }

    override fun applyProtectionReduction(event: EntityDamageEvent, damage: Float): Float {
        if (event.damageSource == DamageSource.ENTITY_ATTACK) {
            val protectionReduction = protectionReduction
            return -(damage * (protectionReduction / 100f))
        }
        return event.damage
    }

    override fun applyProjectileProtectionReduction(event: EntityDamageEvent, damage: Float): Float {
        if (event.damageSource == DamageSource.PROJECTILE) {
            val protectionReduction = projectileProtectionReduction
            return -(damage * (protectionReduction / 100f))
        }
        return event.damage
    }

    override fun applyFireProtectionReduction(event: EntityDamageEvent, damage: Float): Float {
        if (event.damageSource == DamageSource.ON_FIRE) {
            val protectionReduction = fireProtectionReduction
            return -(damage * (protectionReduction / 100f))
        }
        return event.damage
    }

    private val protectionReduction: Float
        private get() {
            var protectionReduction = 0f
            for (content in armorInventory.contents) {
                if (content is ItemArmor) {
                    val enchantment = content.getEnchantment(EnchantmentType.PROTECTION)
                    if (enchantment != null) {
                        protectionReduction += (4 * enchantment.level).toFloat()
                    }
                }
            }
            return protectionReduction
        }
    private val projectileProtectionReduction: Float
        private get() {
            var protectionReduction = 0f
            for (content in armorInventory.contents) {
                if (content is ItemArmor) {
                    val enchantment = content.getEnchantment(EnchantmentType.PROJECTILE_PROTECTION)
                    if (enchantment != null) {
                        protectionReduction += (enchantment.level * 8).toFloat()
                    }
                }
            }
            return protectionReduction
        }
    private val fireProtectionReduction: Float
        private get() {
            var protectionReduction = 0f
            for (content in armorInventory.contents) {
                if (content is ItemArmor) {
                    val enchantment = content.getEnchantment(EnchantmentType.FIRE_PROTECTION)
                    if (enchantment != null) {
                        protectionReduction += (15 * enchantment.level).toFloat()
                    }
                }
            }
            return protectionReduction
        }

    fun kill() {
        damage(EntityDamageEvent(this, this.maxHealth, DamageSource.COMMAND))
    }

    override fun killInternal() {
        if (!isDead) {
            super.killInternal()
            val entityEventPacket = EntityEventPacket()
            entityEventPacket.runtimeEntityId = entityId
            entityEventPacket.type = EntityEventType.DEATH
            playerConnection.sendPacket(entityEventPacket)
            fallDistance = 0f
            highestPosition = 0f
            inAirTicks = 0
            val deathMessage = when (lastDamageSource!!) {
                DamageSource.ENTITY_ATTACK -> this.nameTag + " was slain by " + Objects.requireNonNull(
                    lastDamageEntity,
                )!!.nameTag

                DamageSource.FALL -> this.nameTag + " fell from a high place"
                DamageSource.LAVA -> this.nameTag + " tried to swim in lava"
                DamageSource.FIRE -> this.nameTag + " went up in flames"
                DamageSource.VOID -> this.nameTag + " fell out of the world"
                DamageSource.CACTUS -> this.nameTag + " was pricked to death"
                DamageSource.STARVE -> this.nameTag + " starved to death"
                DamageSource.ON_FIRE -> this.nameTag + " burned to death"
                DamageSource.DROWNING -> this.nameTag + " drowned"
                DamageSource.MAGIC_EFFECT -> this.nameTag + " was killed by magic"
                DamageSource.ENTITY_EXPLODE -> this.nameTag + " blew up"
                DamageSource.PROJECTILE -> this.nameTag + " has been shot"
                DamageSource.API -> this.nameTag + " was killed by setting health to 0"
                DamageSource.COMMAND -> this.nameTag + " died"
            }
            val playerDeathEvent = PlayerDeathEvent(this, deathMessage, true, drops)
            server.pluginManager.callEvent(playerDeathEvent)
            if (playerDeathEvent.isDropInventory) {
                for (drop in playerDeathEvent.drops) {
                    this.world.dropItem(drop, location, null)?.spawn()
                }
                playerInventory.clear()
                cursorInventory.clear()
                armorInventory.clear()
            }
            if (playerDeathEvent.deathMessage.isNotEmpty()) {
                server.broadcastMessage(playerDeathEvent.deathMessage)
            }
            respawnLocation = this.world.spawnLocation.add(0f, this.eyeHeight, 0f)
            val respawnPositionPacket = RespawnPacket()
            respawnPositionPacket.runtimeEntityId = entityId
            respawnPositionPacket.state = RespawnPacket.State.SERVER_SEARCHING
            respawnPositionPacket.position = respawnLocation!!.toVector3f()
            playerConnection.sendPacket(respawnPositionPacket)
            if (playerDeathEvent.deathScreenMessage != null && playerDeathEvent.deathScreenMessage!!.isNotEmpty()
            ) {
                val deathInfoPacket = DeathInfoPacket()
                deathInfoPacket.messageList.add(name)
                deathInfoPacket.causeAttackName = playerDeathEvent.deathScreenMessage // #1e7dfc
                playerConnection.sendPacket(deathInfoPacket)
            }
        }
        lastDamageSource = DamageSource.COMMAND
    }

    fun attackWithItemInHand(target: Entity): Boolean {
        if (target is EntityLiving) {
            val success = false
            var damage = this.attackDamage
            if (hasEffect(EffectType.STRENGTH)) {
                damage *= 0.3f * (getEffect<Effect>(EffectType.STRENGTH)!!.amplifier + 1)
            }
            if (hasEffect(EffectType.WEAKNESS)) {
                damage = -(damage * 0.2f * getEffect<Effect>(EffectType.WEAKNESS)!!.amplifier + 1)
            }
            val sharpness = playerInventory.itemInHand.getEnchantment(EnchantmentType.SHARPNESS) as EnchantmentSharpness
            damage += sharpness.level * 1.25f
            var knockbackLevel = 0
            val knockback = playerInventory.itemInHand.getEnchantment(EnchantmentType.KNOCKBACK) as EnchantmentKnockback
            knockbackLevel += knockback.level.toInt()
            if (damage > 0) {
                val crit = fallDistance > 0 && !isOnGround && !this.isOnLadder && !this.isInWater
                if (crit) {
                    damage *= 1.5.toFloat()
                }
                if (success == target.damage(
                        EntityDamageByEntityEvent(
                            target,
                            this,
                            damage,
                            DamageSource.ENTITY_ATTACK,
                        ),
                    )
                ) {
                    if (knockbackLevel > 0) {
                        val targetVelocity = target.velocity
                        target.velocity = targetVelocity.add(
                            (-sin((this.yaw * Math.PI.toFloat() / 180.0f).toDouble()) * knockbackLevel.toFloat() * 0.3).toFloat(),
                            0.1f,
                            (cos((this.yaw * Math.PI.toFloat() / 180.0f).toDouble()) * knockbackLevel.toFloat() * 0.3).toFloat(),
                        )
                        val ownVelocity = velocity
                        ownVelocity.x = ownVelocity.x * 0.6f
                        ownVelocity.z = ownVelocity.z * 0.6f
                        this.velocity = ownVelocity
                        this.isSprinting = false
                    }
                }
            }
            if (gameMode == GameMode.SURVIVAL) {
                exhaust(0.3f)
            }
            return success
        }
        return false
    }

    fun respawn() {
        if (isDead) {
            val playerRespawnEvent = PlayerRespawnEvent(this, respawnLocation!!)
            server.pluginManager.callEvent(playerRespawnEvent)
            lastDamageEntity = null
            lastDamageSource = null
            lastDamage = 0f
            this.updateMetadata()
            for (attribute in attributes.values) {
                attribute.reset()
            }
            updateAttributes()
            this.teleport(playerRespawnEvent.respawnLocation)
            respawnLocation = null
            this.spawn()
            this.isBurning = false
            this.velocity = Vector.zero()
            playerInventory.sendContents(this)
            cursorInventory.sendContents(this)
            armorInventory.sendContents(this)
            val entityEventPacket = EntityEventPacket()
            entityEventPacket.runtimeEntityId = entityId
            entityEventPacket.type = EntityEventType.RESPAWN
            Server.instance.broadcastPacket(entityEventPacket)
            playerInventory.itemInHand.addToHand(this)
            isDead = false
        }
    }

    val drops: List<Item>
        get() {
            val drops: MutableList<Item> = mutableListOf()
            for (content in playerInventory.contents) {
                if (content.type != ItemType.AIR) {
                    if (content.getEnchantment(EnchantmentType.CURSE_OF_VANISHING) == null) {
                        drops.add(content)
                    }
                }
            }
            for (content in cursorInventory.contents) {
                if (content.type != ItemType.AIR) {
                    if (content.getEnchantment(EnchantmentType.CURSE_OF_VANISHING) == null) {
                        drops.add(content)
                    }
                }
            }
            for (content in armorInventory.contents) {
                if (content.type != ItemType.AIR) {
                    if (content.getEnchantment(EnchantmentType.CURSE_OF_VANISHING) == null) {
                        drops.add(content)
                    }
                }
            }
            return drops
        }

    override fun equals(other: Any?): Boolean {
        return if (other is Player) {
            other.entityId == entityId && other.uuid == uuid
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        var result = playerConnection.hashCode()
        result = 31 * result + uuid.hashCode()
        result = 31 * result + xuid.hashCode()
        return result
    }
}
