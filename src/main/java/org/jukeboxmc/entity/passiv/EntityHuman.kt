package org.jukeboxmc.entity.passiv

import com.nukkitx.math.vector.Vector3f
import com.nukkitx.protocol.bedrock.BedrockPacket
import com.nukkitx.protocol.bedrock.data.GameType
import com.nukkitx.protocol.bedrock.data.SoundEvent
import com.nukkitx.protocol.bedrock.data.entity.EntityFlag
import com.nukkitx.protocol.bedrock.packet.AddPlayerPacket
import org.jukeboxmc.Server
import org.jukeboxmc.entity.EntityLiving
import org.jukeboxmc.entity.EntityType
import org.jukeboxmc.entity.attribute.Attribute
import org.jukeboxmc.entity.attribute.AttributeType
import org.jukeboxmc.inventory.ArmorInventory
import org.jukeboxmc.inventory.InventoryHolder
import org.jukeboxmc.inventory.PlayerInventory
import org.jukeboxmc.player.Player
import org.jukeboxmc.player.info.Device
import org.jukeboxmc.player.info.DeviceInfo
import org.jukeboxmc.player.info.UIProfile
import org.jukeboxmc.player.skin.Skin
import org.jukeboxmc.potion.EffectType
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.util.Utils
import java.util.Random
import java.util.UUID
import org.jukeboxmc.event.player.PlayerFoodLevelChangeEvent

/**
 * @author LucGamesYT
 * @version 1.0
 */
@Suppress("LeakingThis")
open class EntityHuman : EntityLiving(), InventoryHolder {
    var uUID: UUID
    open var skin: Skin? = null
    protected var deviceInfo: DeviceInfo
    protected val playerInventory: PlayerInventory
    protected val armorInventory: ArmorInventory
    protected var foodTicks = 0
    var actionStart: Long = -1
        private set

    init {
        uUID = UUID.randomUUID()
        deviceInfo = DeviceInfo(
            "Unknown",
            UUID.randomUUID().toString(),
            Random().nextLong(),
            Device.DEDICATED,
            UIProfile.CLASSIC,
        )
        playerInventory = PlayerInventory(this)
        armorInventory = ArmorInventory(this)
        this.addAttribute(AttributeType.PLAYER_HUNGER)
        this.addAttribute(AttributeType.PLAYER_SATURATION)
        this.addAttribute(AttributeType.PLAYER_EXHAUSTION)
        this.addAttribute(AttributeType.PLAYER_EXPERIENCE)
        this.addAttribute(AttributeType.PLAYER_LEVEL)
    }

    override val name: String
        get() = this.nameTag
    override val width: Float
        get() = 0.6f
    override val height: Float
        get() = 1.8f
    override val type: EntityType
        get() = EntityType.HUMAN
    override val identifier: Identifier
        get() = Identifier.fromString("minecraft:player")
    override val eyeHeight: Float
        get() = 1.62f

    override fun createSpawnPacket(): BedrockPacket {
        val addPlayerPacket = AddPlayerPacket()
        addPlayerPacket.runtimeEntityId = this.entityId
        addPlayerPacket.uniqueEntityId = this.entityId
        addPlayerPacket.uuid = uUID
        addPlayerPacket.username = name
        addPlayerPacket.platformChatId = deviceInfo.deviceId
        addPlayerPacket.position = this.location.toVector3f()
        addPlayerPacket.motion = this.velocity.toVector3f()
        addPlayerPacket.rotation = Vector3f.from(
            this.location.pitch,
            this.location.yaw,
            this.location.yaw,
        )
        addPlayerPacket.gameType = GameType.SURVIVAL
        addPlayerPacket.metadata.putAll(this.metadata.getEntityDataMap())
        addPlayerPacket.deviceId = deviceInfo.deviceId
        addPlayerPacket.hand = playerInventory.itemInHand.toItemData()
        return addPlayerPacket
    }

    override fun spawn(player: Player): EntityHuman {
        if (this !== player) {
            super.spawn(player)
            armorInventory.sendContents(player)
        }
        return this
    }

    override fun spawn(): EntityHuman {
        for (player in this.world!!.players) {
            if (this.dimension == player.dimension) {
                this.spawn(player)
            }
        }
        return this
    }

    override fun despawn(player: Player): EntityHuman {
        if (this !== player) {
            super.despawn(player)
        }
        return this
    }

    override fun despawn(): EntityHuman {
        for (player in this.world!!.players) {
            if (this.dimension == player.dimension) {
                this.despawn(player)
            }
        }
        return this
    }

    fun getDeviceInfo(): DeviceInfo {
        return deviceInfo
    }

    fun setDeviceInfo(deviceInfo: DeviceInfo) {
        this.deviceInfo = deviceInfo
    }

    val inventory: PlayerInventory
        get() = playerInventory

    fun getArmorInventory(): ArmorInventory {
        return armorInventory
    }

    var isSneaking: Boolean
        // =========== Metadata ===========
        get() = this.metadata.getFlag(EntityFlag.SNEAKING)
        set(value) {
            if (value != isSneaking) {
                this.updateMetadata(this.metadata.setFlag(EntityFlag.SNEAKING, value))
            }
        }
    var isSprinting: Boolean
        get() = this.metadata.getFlag(EntityFlag.SPRINTING)
        set(value) {
            if (value != isSprinting) {
                this.updateMetadata(this.metadata.setFlag(EntityFlag.SPRINTING, value))
                this.movement = if (value) this.movement * 1.3f else this.movement / 1.3f
                if (this.hasEffect(EffectType.SPEED)) {
                    val movement: Float = this.movement
                    this.movement = if (value) movement * 1.3f else movement
                }
            }
        }
    var isSwimming: Boolean
        get() = this.metadata.getFlag(EntityFlag.SWIMMING)
        set(value) {
            if (value != isSwimming) {
                this.updateMetadata(this.metadata.setFlag(EntityFlag.SWIMMING, value))
            }
        }
    var isGliding: Boolean
        get() = this.metadata.getFlag(EntityFlag.GLIDING)
        set(value) {
            if (value != isGliding) {
                this.updateMetadata(this.metadata.setFlag(EntityFlag.GLIDING, value))
            }
        }

    fun hasAction(): Boolean {
        return this.metadata.getFlag(EntityFlag.USING_ITEM)
    }

    fun setAction(value: Boolean) {
        this.updateMetadata(this.metadata.setFlag(EntityFlag.USING_ITEM, value))
        actionStart = if (value) {
            Server.instance.currentTick
        } else {
            -1
        }
    }

    fun resetActionStart() {
        actionStart = Server.instance.currentTick
    }

    val isHungry: Boolean
        // =========== Attribute ===========
        get() {
            val attribute: Attribute = this.getAttribute(AttributeType.PLAYER_HUNGER)!!
            return attribute.getCurrentValue() < attribute.maxValue
        }
    var hunger: Int
        get() = this.getAttributeValue(AttributeType.PLAYER_HUNGER).toInt()
        set(value) {
            val attribute: Attribute = this.getAttribute(AttributeType.PLAYER_HUNGER)!!
            val old = attribute.getCurrentValue()
            this.setAttributes(
                AttributeType.PLAYER_HUNGER,
                Utils.clamp(value.toFloat(), attribute.minValue, attribute.maxValue),
            )
            if (old < 17 && value >= 17 || old < 6 && value >= 6 || old > 0 && value == 0) {
                foodTicks = 0
            }
        }

    fun addHunger(value: Int) {
        hunger += value
    }

    var saturation: Float
        get() = this.getAttributeValue(AttributeType.PLAYER_SATURATION)
        set(value) {
            val attribute: Attribute = this.getAttribute(AttributeType.PLAYER_SATURATION)!!
            this.setAttributes(
                AttributeType.PLAYER_SATURATION,
                Utils.clamp(value, attribute.minValue, attribute.maxValue),
            )
        }
    var exhaustion: Float
        get() = this.getAttributeValue(AttributeType.PLAYER_EXHAUSTION)
        set(value) {
            this.setAttributes(AttributeType.PLAYER_EXHAUSTION, value)
        }
    open var experience: Float
        get() = this.getAttributeValue(AttributeType.PLAYER_EXPERIENCE)
        set(value) {
            this.setAttributes(AttributeType.PLAYER_EXPERIENCE, value)
        }
    open var level: Float
        get() = this.getAttributeValue(AttributeType.PLAYER_LEVEL)
        set(value) {
            this.setAttributes(AttributeType.PLAYER_LEVEL, value)
        }

    fun addExperience(value: Int, playLevelUpSound: Boolean) {
        if (value == 0) return
        val now = experience.toInt()
        var added = now + value
        var level = level.toInt()
        var most = calculateRequireExperience(level)
        while (added >= most) { // Level Up!
            added -= most
            level++
            most = calculateRequireExperience(level)
        }
        exhaustion = added.toFloat()
        this.level = level.toFloat()
        if (playLevelUpSound) {
            this.location.world?.playSound(this.location, SoundEvent.LEVELUP, Math.min(7, level / 5) shl 28)
        }
    }

    fun calculateRequireExperience(level: Int): Int {
        return if (level >= 30) {
            112 + (level - 30) * 9
        } else if (level >= 15) {
            37 + (level - 15) * 5
        } else {
            7 + level * 2
        }
    }

    fun exhaust(value: Float) {
        if (this.hasEffect(EffectType.SATURATION)) {
            return
        }
        var exhaustion = exhaustion + value
        while (exhaustion >= 4) {
            exhaustion -= 4f
            var saturation = saturation
            if (saturation > 0) {
                saturation = Math.max(0f, saturation - 1)
                this.saturation = saturation
            } else {
                val hunger = hunger
                if (hunger > 0) {
                    if (this is Player) {
                        val player = this
                        val playerFoodLevelChangeEvent = PlayerFoodLevelChangeEvent(player, hunger, saturation)
                        Server.instance.pluginManager.callEvent(playerFoodLevelChangeEvent)
                        if (playerFoodLevelChangeEvent.isCancelled) {
                            player.updateAttributes()
                            return
                        }
                        this.hunger = Math.max(0, playerFoodLevelChangeEvent.foodLevel - 1)
                    } else {
                        this.hunger = Math.max(0, hunger - 1)
                    }
                }
            }
        }
        this.exhaustion = exhaustion
    }
}
