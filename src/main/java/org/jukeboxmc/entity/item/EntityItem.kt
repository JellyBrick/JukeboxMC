package org.jukeboxmc.entity.item

import com.nukkitx.protocol.bedrock.packet.AddItemEntityPacket
import com.nukkitx.protocol.bedrock.packet.TakeItemEntityPacket
import org.jukeboxmc.Server
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.entity.Entity
import org.jukeboxmc.entity.EntityMoveable
import org.jukeboxmc.entity.EntityType
import org.jukeboxmc.event.player.PlayerPickupItemEvent
import org.jukeboxmc.item.Item
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import java.util.concurrent.TimeUnit

/**
 * @author LucGamesYT
 * @version 1.0
 */
class EntityItem : EntityMoveable() {
    private var item: Item? = null
    var pickupDelay: Long = 0
        private set
    private var isReset = false
    var playerHasThrown: Player? = null
    override fun update(currentTick: Long) {
        super.update(currentTick)
        if (this.isClosed) {
            return
        }
        if (!this.isImmobile) {
            val blockTypeLayer0: BlockType = this.location.world!!.getBlock(
                this.location.blockX,
                this.boundingBox.maxY.toInt(),
                this.location.blockZ,
                0,
                this.location.dimension,
            ).type
            if (blockTypeLayer0 == BlockType.FLOWING_WATER || blockTypeLayer0 == BlockType.WATER) {
                this.velocity.y = this.velocity.y - gravity * -0.015f
            } else {
                this.velocity.y = this.velocity.y - gravity
            }
            this.checkObstruction(this.location.x, this.location.y, this.location.z)
            this.move(this.velocity)
            var friction: Double = (1 - this.drag).toDouble()
            if (this.isOnGround && (Math.abs(this.velocity.x) > 0.00001 || Math.abs(this.velocity.z) > 0.00001)) {
                friction *= 0.6
            }
            this.velocity.x = (this.velocity.x * friction).toFloat()
            this.velocity.y = this.velocity.y * (1 - this.drag)
            this.velocity.z = (this.velocity.z * friction).toFloat()
            if (this.isOnGround) {
                this.velocity.y = this.velocity.y * -0.5f
            }
            this.updateMovement()
        }
        if (this.isCollided && !isReset && this.velocity.squaredLength() < 0.01f) {
            this.velocity = Vector.zero()
            isReset = true
        }
        if (this.age >= TimeUnit.MINUTES.toMillis(5) / 50 || this.location.y <= -64) {
            this.close()
        }
    }

    override val name: String
        get() = "EntityItem"
    override val width: Float
        get() = 0.25f
    override val height: Float
        get() = 0.25f
    override var gravity: Float = 0.04f
    override val type: EntityType
        get() = EntityType.ITEM
    override val identifier: Identifier
        get() = Identifier.fromString("minecraft:item")

    override fun createSpawnPacket(): AddItemEntityPacket {
        val addItemEntityPacket = AddItemEntityPacket()
        addItemEntityPacket.runtimeEntityId = this.entityId
        addItemEntityPacket.uniqueEntityId = this.entityId
        addItemEntityPacket.itemInHand = item!!.toItemData()
        addItemEntityPacket.position = this.location.toVector3f()
        addItemEntityPacket.motion = this.velocity.toVector3f()
        addItemEntityPacket.metadata.putAll(this.metadata.getEntityDataMap())
        return addItemEntityPacket
    }

    override fun onCollideWithPlayer(player: Player) {
        if (Server.instance.currentTick > pickupDelay && !this.isClosed && !player.isDead) {
            val playerPickupItemEvent = PlayerPickupItemEvent(player, item!!)
            Server.instance.pluginManager.callEvent(playerPickupItemEvent)
            if (playerPickupItemEvent.isCancelled || !player.inventory.canAddItem(playerPickupItemEvent.item)) {
                return
            }
            val takeItemEntityPacket = TakeItemEntityPacket()
            takeItemEntityPacket.runtimeEntityId = player.entityId
            takeItemEntityPacket.itemRuntimeEntityId = this.entityId
            Server.instance.broadcastPacket(takeItemEntityPacket)
            this.close()
            player.inventory.addItem(playerPickupItemEvent.item)
            player.inventory.sendContents(player)
        }
    }

    override fun canCollideWith(entity: Entity?): Boolean {
        return false
    }

    fun getItem(): Item {
        return item!!.clone()
    }

    fun setItem(item: Item?) {
        this.item = item
    }

    fun setPickupDelay(duration: Long, timeUnit: TimeUnit) {
        pickupDelay = Server.instance.currentTick + timeUnit.toMillis(duration) / 50
    }
}
