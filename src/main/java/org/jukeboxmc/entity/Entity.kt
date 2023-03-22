package org.jukeboxmc.entity

import com.nukkitx.math.vector.Vector3f
import com.nukkitx.protocol.bedrock.BedrockPacket
import com.nukkitx.protocol.bedrock.data.entity.EntityData
import com.nukkitx.protocol.bedrock.data.entity.EntityFlag
import com.nukkitx.protocol.bedrock.packet.AddEntityPacket
import com.nukkitx.protocol.bedrock.packet.RemoveEntityPacket
import com.nukkitx.protocol.bedrock.packet.SetEntityDataPacket
import com.nukkitx.protocol.bedrock.packet.SetEntityMotionPacket
import org.jukeboxmc.Server
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockWater
import org.jukeboxmc.block.direction.Direction
import org.jukeboxmc.entity.metadata.Metadata
import org.jukeboxmc.event.entity.EntityDamageByEntityEvent
import org.jukeboxmc.event.entity.EntityDamageEvent
import org.jukeboxmc.event.entity.EntityDamageEvent.DamageSource
import org.jukeboxmc.event.entity.EntityDespawnEvent
import org.jukeboxmc.event.entity.EntitySpawnEvent
import org.jukeboxmc.event.entity.EntityVelocityEvent
import org.jukeboxmc.math.AxisAlignedBB
import org.jukeboxmc.math.Location
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.GameMode
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.Dimension
import org.jukeboxmc.world.World
import org.jukeboxmc.world.chunk.Chunk
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author LucGamesYT
 * @version 1.0
 */
abstract class Entity {
    var entityId: Long
        protected set
    val metadata: Metadata
    var location: Location = Location(null, Vector(0, 0, 0))
        set(location) {
            field = location
            recalculateBoundingBox()
        }
    var lastLocation: Location
    protected var velocity: Vector
    var lastVector: Vector
    val boundingBox: AxisAlignedBB
    var isOnGround = false
    var isClosed = false
        protected set
    var isDead = false
        protected set
    var age = 0
        protected set
    open var fallDistance = 0f
        protected set
    var fireTicks: Long = 0
        protected set
    private val spawnedFor: MutableSet<Long> = HashSet()

    init {
        entityId = entityCount++
        metadata = Metadata()
        metadata.setLong(EntityData.PLAYER_INDEX, 0)
        metadata.setShort(EntityData.AIR_SUPPLY, 400.toShort())
        metadata.setShort(EntityData.MAX_AIR_SUPPLY, 400.toShort())
        metadata.setFloat(EntityData.SCALE, 1f)
        metadata.setFloat(EntityData.BOUNDING_BOX_WIDTH, width)
        metadata.setFloat(EntityData.BOUNDING_BOX_HEIGHT, height)
        metadata.setFlag(EntityFlag.HAS_GRAVITY, true)
        metadata.setFlag(EntityFlag.HAS_COLLISION, true)
        metadata.setFlag(EntityFlag.CAN_CLIMB, true)
        metadata.setFlag(EntityFlag.BREATHING, true)
        location = Objects.requireNonNull<World?>(Server.instance.defaultWorld).getSpawnLocation()
        lastLocation = Objects.requireNonNull<World?>(Server.instance.defaultWorld).getSpawnLocation()
        velocity = Vector(0, 0, 0, location.dimension)
        lastVector = Vector(0, 0, 0, location.dimension)
        boundingBox = AxisAlignedBB(0f, 0f, 0f, 0f, 0f, 0f)
        recalculateBoundingBox()
    }

    open fun update(currentTick: Long) {
        age++
        if (fireTicks > 0) {
            if (fireTicks % 20 == 0L) {
                damage(EntityDamageEvent(this, 1f, DamageSource.ON_FIRE))
            }
            fireTicks--
            if (fireTicks == 0L) {
                isBurning = false
            }
        }
    }

    abstract val name: String
    abstract val width: Float
    abstract val height: Float
    abstract val type: EntityType
    abstract val identifier: Identifier
    open val eyeHeight: Float
        get() = height / 2 + 0.1f

    open fun createSpawnPacket(): BedrockPacket {
        val addEntityPacket = AddEntityPacket()
        addEntityPacket.runtimeEntityId = entityId
        addEntityPacket.uniqueEntityId = entityId
        addEntityPacket.identifier = identifier.fullName
        addEntityPacket.position = location.add(0f, eyeHeight, 0f).toVector3f()
        addEntityPacket.motion = velocity.toVector3f()
        addEntityPacket.rotation = Vector3f.from(location.pitch, location.yaw, location.yaw)
        addEntityPacket.metadata.putAll(metadata.getEntityDataMap())
        return addEntityPacket
    }

    open fun canCollideWith(entity: Entity?): Boolean {
        return false
    }

    fun canPassThrough(): Boolean {
        return true
    }

    protected open fun fall() {}
    fun interact(player: Player?, vector: Vector?) {}
    open fun spawn(player: Player): Entity {
        if (!spawnedFor.contains(player.entityId)) {
            val entitySpawnEvent = EntitySpawnEvent(this)
            Server.instance.pluginManager.callEvent(entitySpawnEvent)
            if (entitySpawnEvent.isCancelled) {
                return this
            }
            val entity: Entity = entitySpawnEvent.getEntity()
            world!!.addEntity(entity)
            chunk!!.addEntity(entity)
            player.playerConnection.sendPacket(entity.createSpawnPacket())
            spawnedFor.add(player.entityId)
        }
        return this
    }

    open fun spawn(): Entity {
        for (player in world!!.players) {
            this.spawn(player)
        }
        return this
    }

    open fun despawn(player: Player): Entity {
        if (spawnedFor.contains(player.entityId)) {
            val entityDespawnEvent = EntityDespawnEvent(this)
            Server.instance.pluginManager.callEvent(entityDespawnEvent)
            if (entityDespawnEvent.isCancelled) {
                return this
            }
            val removeEntityPacket = RemoveEntityPacket()
            removeEntityPacket.uniqueEntityId = entityDespawnEvent.getEntity().entityId
            player.playerConnection.sendPacket(removeEntityPacket)
            spawnedFor.remove(player.entityId)
        }
        return this
    }

    open fun despawn(): Entity {
        for (player in world!!.players) {
            this.despawn(player)
        }
        return this
    }

    fun close() {
        this.despawn()
        chunk!!.removeEntity(this)
        world!!.removeEntity(this)
        isClosed = true
        isDead = true
    }

    fun getVelocity(): Vector {
        return velocity
    }

    fun setVelocity(velocity: Vector, sendVelocity: Boolean) {
        val entityVelocityEvent = EntityVelocityEvent(this, velocity)
        Server.instance.pluginManager.callEvent(entityVelocityEvent)
        if (entityVelocityEvent.isCancelled) {
            return
        }
        this.velocity = entityVelocityEvent.velocity
        if (sendVelocity) {
            val entityVelocityPacket = SetEntityMotionPacket()
            entityVelocityPacket.runtimeEntityId = entityVelocityEvent.getEntity().entityId
            entityVelocityPacket.motion = entityVelocityEvent.velocity.toVector3f()
            Server.instance.broadcastPacket(entityVelocityPacket)
        }
    }

    fun setVelocity(velocity: Vector) {
        this.setVelocity(velocity, true)
    }

    val world: World?
        get() = location.world
    val worldNonNull: World
        get() {
            if (location.world == null /*|| !location.world!!.isLoaded TODO*/) {
                throw IllegalStateException("World is not loaded")
            }
            return location.world!!
        }
    val x: Float
        get() = location.x
    val y: Float
        get() = location.y
    val z: Float
        get() = location.z
    val blockX: Int
        get() = location.blockX
    val blockY: Int
        get() = location.blockY
    val blockZ: Int
        get() = location.blockZ
    var yaw: Float
        get() = location.yaw
        set(yaw) {
            location.yaw = yaw
        }
    var pitch: Float
        get() = location.pitch
        set(pitch) {
            location.pitch = pitch
        }
    val chunkX: Int
        get() = location.blockX shr 4
    val chunkZ: Int
        get() = location.blockZ shr 4
    val chunk: Chunk?
        get() = location.world?.getChunk(location.chunkX, location.chunkZ, location.dimension)
    val loadedChunk: Chunk?
        get() = location.world?.getLoadedChunk(location.chunkX, location.chunkZ, location.dimension)
    val dimension: Dimension
        get() = location.dimension

    fun recalculateBoundingBox() {
        val height = height * scale
        val radius = width * scale / 2
        boundingBox.setBounds(
            location.x - radius,
            location.y,
            location.z - radius,
            location.x + radius,
            location.y + height,
            location.z + radius,
        )
        metadata.setFloat(EntityData.BOUNDING_BOX_HEIGHT, this.height)
        metadata.setFloat(EntityData.BOUNDING_BOX_WIDTH, width)
    }

    val direction: Direction
        get() {
            var rotation = (location.yaw % 360).toDouble()
            if (rotation < 0) {
                rotation += 360.0
            }
            return if (45 <= rotation && rotation < 135) {
                Direction.WEST
            } else if (135 <= rotation && rotation < 225) {
                Direction.NORTH
            } else if (225 <= rotation && rotation < 315) {
                Direction.EAST
            } else {
                Direction.SOUTH
            }
        }
    var maxAirSupply: Short
        // ============ Metadata =============
        get() = metadata.getShort(EntityData.AIR_SUPPLY)
        set(value) {
            if (value != maxAirSupply) {
                this.updateMetadata(metadata.setShort(EntityData.AIR_SUPPLY, value))
            }
        }
    var scale: Float
        get() = metadata.getFloat(EntityData.SCALE)
        set(value) {
            if (value != scale) {
                this.updateMetadata(metadata.setFloat(EntityData.SCALE, value))
            }
        }

    fun hasCollision(): Boolean {
        return metadata.getFlag(EntityFlag.HAS_COLLISION)
    }

    fun setCollision(value: Boolean) {
        if (hasCollision() != value) {
            this.updateMetadata(metadata.setFlag(EntityFlag.HAS_COLLISION, value))
        }
    }

    fun hasGravity(): Boolean {
        return metadata.getFlag(EntityFlag.HAS_GRAVITY)
    }

    fun setGravity(value: Boolean) {
        if (hasGravity() != value) {
            this.updateMetadata(metadata.setFlag(EntityFlag.HAS_GRAVITY, value))
        }
    }

    var nameTag: String
        get() = metadata.getString(EntityData.NAMETAG)
        set(value) {
            this.updateMetadata(metadata.setString(EntityData.NAMETAG, value))
        }
    var isNameTagVisible: Boolean
        get() = metadata.getFlag(EntityFlag.CAN_SHOW_NAME)
        set(value) {
            if (value != isNameTagVisible) {
                this.updateMetadata(metadata.setFlag(EntityFlag.CAN_SHOW_NAME, value))
            }
        }
    var isNameTagAlwaysVisible: Boolean
        get() = metadata.getFlag(EntityFlag.ALWAYS_SHOW_NAME)
        set(value) {
            if (value != isNameTagAlwaysVisible) {
                this.updateMetadata(metadata.setFlag(EntityFlag.ALWAYS_SHOW_NAME, value))
            }
        }

    fun canClimb(): Boolean {
        return metadata.getFlag(EntityFlag.CAN_CLIMB)
    }

    fun setCanClimb(value: Boolean) {
        if (value != canClimb()) {
            this.updateMetadata(metadata.setFlag(EntityFlag.CAN_CLIMB, value))
        }
    }

    var isInvisible: Boolean
        get() = metadata.getFlag(EntityFlag.INVISIBLE)
        set(value) {
            if (value != isInvisible) {
                this.updateMetadata(metadata.setFlag(EntityFlag.INVISIBLE, value))
            }
        }
    var isBurning: Boolean
        get() = metadata.getFlag(EntityFlag.ON_FIRE)
        set(value) {
            if (value != isBurning) {
                this.updateMetadata(metadata.setFlag(EntityFlag.ON_FIRE, value))
            }
        }

    fun setBurning(value: Long, timeUnit: TimeUnit) {
        val newFireTicks = (timeUnit.toMillis(value) / 50).toInt()
        if (newFireTicks > fireTicks) {
            fireTicks = newFireTicks.toLong()
            isBurning = true
        } else if (newFireTicks == 0) {
            fireTicks = 0
            isBurning = false
        }
    }

    var isImmobile: Boolean
        get() = metadata.getFlag(EntityFlag.NO_AI)
        set(value) {
            if (value != isImmobile) {
                this.updateMetadata(metadata.setFlag(EntityFlag.NO_AI, value))
            }
        }

    fun updateMetadata(metadata: Metadata) {
        val setEntityDataPacket = SetEntityDataPacket()
        setEntityDataPacket.runtimeEntityId = entityId
        setEntityDataPacket.metadata.putAll(metadata.getEntityDataMap())
        setEntityDataPacket.tick = Server.instance.currentTick
        Server.instance.broadcastPacket(setEntityDataPacket)
    }

    fun updateMetadata() {
        val setEntityDataPacket = SetEntityDataPacket()
        setEntityDataPacket.runtimeEntityId = entityId
        setEntityDataPacket.metadata.putAll(metadata.getEntityDataMap())
        setEntityDataPacket.tick = Server.instance.currentTick
        Server.instance.broadcastPacket(setEntityDataPacket)
    }

    protected val isOnLadder: Boolean
        protected get() {
            val location = this.location
            val loadedChunk =
                location.world!!.getLoadedChunk(location.chunkX, location.chunkZ, location.dimension)
                    ?: return false
            val block = loadedChunk.getBlock(location.blockX, location.blockY, location.blockZ, 0)
            return block.type == BlockType.LADDER
        }
    val isInWater: Boolean
        get() {
            val eyeLocation: Vector = location.add(0f, eyeHeight, 0f)
            val loadedChunk = world!!.getLoadedChunk(eyeLocation.chunkX, eyeLocation.chunkZ, location.dimension)
                ?: return false
            val block = loadedChunk.getBlock(eyeLocation.blockX, eyeLocation.blockY, eyeLocation.blockZ, 0)
            if (block.type == BlockType.WATER || block.type == BlockType.FLOWING_WATER) {
                val yLiquid = (block.location.y + 1 + (block as BlockWater).liquidDepth - 0.12).toFloat()
                return eyeLocation.y < yLiquid
            }
            return false
        }

    fun setOnFire(value: Int, timeUnit: TimeUnit) {
        val ticks = timeUnit.toMillis(value.toLong()) / 50
        if (ticks > fireTicks) {
            fireTicks = ticks
        }
    }

    val isOnFire: Boolean
        get() = fireTicks > 0

    open fun damage(event: EntityDamageEvent): Boolean {
        if (isDead) {
            return false
        }
        if (event is EntityDamageByEntityEvent) {
            val damager: Entity = (event as EntityDamageByEntityEvent).damager
            if (damager is Player) {
                return damager.gameMode != GameMode.SPECTATOR
            }
        }
        Server.instance.pluginManager.callEvent(event)
        return !event.isCancelled
    }

    companion object {
        var entityCount: Long = 1
        fun create(entityType: EntityType): Entity? {
            return try {
                EntityRegistry.getEntityClass(entityType).getConstructor().newInstance()
            } catch (e: ReflectiveOperationException) {
                null
            }
        }

        inline fun <reified T : Entity> create(entityType: EntityType): T? =
            create(entityType) as? T
    }
}
