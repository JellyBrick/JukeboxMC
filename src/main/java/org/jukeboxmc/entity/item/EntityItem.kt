package org.jukeboxmc.entity.item

import java.util.concurrent.TimeUnit
import org.jukeboxmc.Server
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.entity.Entity
import org.jukeboxmc.entity.EntityType
import org.jukeboxmc.item.Item
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier

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
        if (this.isClosed()) {
            return
        }
        if (!this.isImmobile()) {
            val blockTypeLayer0: BlockType = this.location.getWorld().getBlock(
                this.location.getBlockX(),
                this.boundingBox.getMaxY().toInt(),
                this.location.getBlockZ(),
                0,
                this.location.getDimension()
            ).getType()
            if (blockTypeLayer0 == BlockType.FLOWING_WATER || blockTypeLayer0 == BlockType.WATER) {
                this.velocity.setY(this.velocity.getY() - gravity * -0.015f)
            } else {
                this.velocity.setY(this.velocity.getY() - gravity)
            }
            this.checkObstruction(this.location.getX(), this.location.getY(), this.location.getZ())
            this.move(this.velocity)
            var friction: Double = (1 - this.getDrag()).toDouble()
            if (this.onGround && (Math.abs(this.velocity.getX()) > 0.00001 || Math.abs(this.velocity.getZ()) > 0.00001)) {
                friction *= 0.6
            }
            this.velocity.setX((this.velocity.getX() * friction).toFloat())
            this.velocity.setY(this.velocity.getY() * (1 - this.getDrag()))
            this.velocity.setZ((this.velocity.getZ() * friction).toFloat())
            if (this.onGround) {
                this.velocity.setY(this.velocity.getY() * -0.5f)
            }
            this.updateMovement()
        }
        if (this.isCollided && !isReset && this.velocity.squaredLength() < 0.01f) {
            this.setVelocity(Vector.Companion.zero())
            isReset = true
        }
        if (this.age >= TimeUnit.MINUTES.toMillis(5) / 50 || this.location.getY() <= -64) {
            this.close()
        }
    }

    val name: String
        get() = "EntityItem"
    val width: Float
        get() = 0.25f
    val height: Float
        get() = 0.25f
    val gravity: Float
        get() = 0.04f
    val type: EntityType
        get() = EntityType.ITEM
    val identifier: Identifier
        get() = Identifier.Companion.fromString("minecraft:item")

    override fun createSpawnPacket(): AddItemEntityPacket {
        val addItemEntityPacket = AddItemEntityPacket()
        addItemEntityPacket.setRuntimeEntityId(this.entityId)
        addItemEntityPacket.setUniqueEntityId(this.entityId)
        addItemEntityPacket.setItemInHand(item!!.toItemData())
        addItemEntityPacket.setPosition(this.location.toVector3f())
        addItemEntityPacket.setMotion(this.velocity.toVector3f())
        addItemEntityPacket.getMetadata().putAll(this.metadata.getEntityDataMap())
        return addItemEntityPacket
    }

    override fun onCollideWithPlayer(player: Player) {
        if (Server.Companion.getInstance().getCurrentTick() > pickupDelay && !this.isClosed() && !player.isDead) {
            val playerPickupItemEvent = PlayerPickupItemEvent(player, item)
            Server.Companion.getInstance().getPluginManager().callEvent(playerPickupItemEvent)
            if (playerPickupItemEvent.isCancelled() || !player.inventory.canAddItem(playerPickupItemEvent.getItem())) {
                return
            }
            val takeItemEntityPacket = TakeItemEntityPacket()
            takeItemEntityPacket.setRuntimeEntityId(player.entityId)
            takeItemEntityPacket.setItemRuntimeEntityId(this.entityId)
            Server.Companion.getInstance().broadcastPacket(takeItemEntityPacket)
            this.close()
            player.inventory.addItem(playerPickupItemEvent.getItem())
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
        pickupDelay = Server.Companion.getInstance().getCurrentTick() + timeUnit.toMillis(duration) / 50
    }
}