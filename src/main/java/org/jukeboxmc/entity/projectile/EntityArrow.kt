package org.jukeboxmc.entity.projectile

import com.nukkitx.protocol.bedrock.data.LevelEventType
import java.util.concurrent.TimeUnit
import org.jukeboxmc.Server
import org.jukeboxmc.entity.Entity
import org.jukeboxmc.entity.EntityType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class EntityArrow : EntityProjectile() {
    var pickupDelay: Long = 0
        private set
    private var canBePickedUp = false
    private var wasInfinityArrow = false
    var force = 0f
    var powerModifier = 0
    var punchModifier = 0
    var flameModifier = 0
    override fun update(currentTick: Long) {
        super.update(currentTick)
        if (onGround || isCollided || hadCollision) {
            canBePickedUp = true
        }
        if (age >= TimeUnit.MINUTES.toMillis(5) / 50) {
            close()
        }
    }

    val name: String
        get() = "Arrow"
    val width: Float
        get() = 0.5f
    val height: Float
        get() = 0.5f
    val type: EntityType
        get() = EntityType.ARROW
    val identifier: Identifier
        get() = Identifier.Companion.fromString("minecraft:arrow")

    override fun onCollideWithPlayer(player: Player) {
        if (Server.Companion.getInstance()
                .getCurrentTick() > pickupDelay && canBePickedUp && !closed && !player.isDead
        ) {
            val arrow: Item = Item.Companion.create<Item>(ItemType.ARROW)
            if (!player.inventory.canAddItem(arrow)) {
                return
            }
            val pickupItemEvent = PlayerPickupItemEvent(player, arrow)
            player.world.server.pluginManager.callEvent(pickupItemEvent)
            if (pickupItemEvent.isCancelled()) {
                return
            }
            close()
            player.world.sendLevelEvent(player.location, LevelEventType.SOUND_INFINITY_ARROW_PICKUP)
            if (!wasInfinityArrow) {
                player.inventory.addItem(arrow)
            }
            player.inventory.sendContents(player)
        }
    }

    override fun applyCustomKnockback(hitEntity: Entity) {
        if (punchModifier > 0) {
            val sqrtMotion = Math.sqrt((velocity.x * velocity.x + velocity.z * velocity.z).toDouble()).toFloat()
            if (sqrtMotion > 0.0f) {
                val toAdd = Vector(
                    velocity.x * punchModifier * 0.6f / sqrtMotion,
                    0.1f,
                    velocity.z * punchModifier * 0.6f / sqrtMotion
                )
                hitEntity.velocity = hitEntity.velocity.add(toAdd.x, toAdd.y, toAdd.z)
            }
        }
    }

    override fun applyCustomDamageEffects(hitEntity: Entity) {
        if (flameModifier > 0 && hitEntity is EntityLiving) {
            hitEntity.setBurning(5, TimeUnit.SECONDS)
        }
    }

    override val damage: Float
        get() = if (powerModifier > 0) {
            2 + (powerModifier * 0.5f + 0.5f)
        } else 2

    fun setPickupDelay(duration: Long, timeUnit: TimeUnit) {
        pickupDelay = Server.Companion.getInstance().getCurrentTick() + timeUnit.toMillis(duration) / 50
    }

    fun wasInfinityArrow(): Boolean {
        return wasInfinityArrow
    }

    fun setWasInfinityArrow(wasInfinityArrow: Boolean) {
        this.wasInfinityArrow = wasInfinityArrow
    }
}