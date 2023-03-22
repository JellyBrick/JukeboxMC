package org.jukeboxmc.item.behavior

import com.nukkitx.protocol.bedrock.data.SoundEvent
import org.jukeboxmc.entity.Entity
import org.jukeboxmc.entity.EntityType
import org.jukeboxmc.entity.projectile.EntityFishingHook
import org.jukeboxmc.event.entity.ProjectileLaunchEvent
import org.jukeboxmc.item.Burnable
import org.jukeboxmc.item.Durability
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import java.time.Duration
import java.util.*

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemFishingRod : Item, Durability, Burnable {
    constructor(identifier: Identifier) : super(identifier)
    constructor(itemType: ItemType) : super(itemType)

    override fun useInAir(player: Player, clickVector: Vector): Boolean {
        if (player.entityFishingHook == null) {
            val entityFishingHook =
                Objects.requireNonNull<EntityFishingHook>(Entity.create<EntityFishingHook>(EntityType.FISHING_HOOK))
            entityFishingHook.setShooter(player)
            entityFishingHook.location = player.location.add(0f, player.eyeHeight, 0f)
            val force = 1.6f
            entityFishingHook.setVelocity(
                Vector(
                    (-Math.sin(Math.toRadians(player.yaw.toDouble())) * Math.cos(Math.toRadians(player.pitch.toDouble())) * force * force).toFloat(),
                    (
                        -Math.sin(
                            Math.toRadians(player.pitch.toDouble()),
                        ) * force * force + 0.4f
                        ).toFloat(),
                    (
                        Math.cos(Math.toRadians(player.yaw.toDouble())) * Math.cos(
                            Math.toRadians(player.pitch.toDouble()),
                        ) * force * force
                        ).toFloat(),
                ),
                false,
            )
            val projectileLaunchEvent = ProjectileLaunchEvent(entityFishingHook, ProjectileLaunchEvent.Cause.SNOWBALL)
            player.server.pluginManager.callEvent(projectileLaunchEvent)
            if (!projectileLaunchEvent.isCancelled) {
                entityFishingHook.spawn()
                this.updateItem(player, 1)
                player.entityFishingHook = entityFishingHook
                player.world?.playSound(player.location, SoundEvent.THROW, -1, "minecraft:player", false, false)
                return true
            }
            return false
        } else {
            player.entityFishingHook?.close()
            player.entityFishingHook = null
        }
        return true
    }

    override val burnTime: Duration?
        get() = Duration.ofMillis(300)
    override val maxDurability: Int
        get() = 384
}
