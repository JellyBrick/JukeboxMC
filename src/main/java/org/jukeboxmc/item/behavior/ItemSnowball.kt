package org.jukeboxmc.item.behavior

import com.nukkitx.protocol.bedrock.data.SoundEvent
import org.jukeboxmc.entity.Entity
import org.jukeboxmc.entity.EntityType
import org.jukeboxmc.entity.projectile.EntitySnowball
import org.jukeboxmc.event.entity.ProjectileLaunchEvent
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.GameMode
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import java.util.*

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemSnowball : Item {
    constructor(identifier: Identifier) : super(identifier)
    constructor(itemType: ItemType) : super(itemType)

    override fun useInAir(player: Player, clickVector: Vector): Boolean {
        val entitySnowball =
            Objects.requireNonNull<EntitySnowball>(Entity.create<EntitySnowball>(EntityType.SNOWBALL))
        entitySnowball.shooter = player
        entitySnowball.location = player.location.add(0f, player.eyeHeight, 0f)
        entitySnowball.setVelocity(clickVector.multiply(1.5f, 1.5f, 1.5f), false)
        entitySnowball.yaw = player.yaw
        entitySnowball.pitch = player.pitch
        val projectileLaunchEvent = ProjectileLaunchEvent(entitySnowball, ProjectileLaunchEvent.Cause.SNOWBALL)
        player.server.pluginManager.callEvent(projectileLaunchEvent)
        if (!projectileLaunchEvent.isCancelled) {
            this.updateItem(player, 1)
            if (player.gameMode == GameMode.SURVIVAL) {
                player.inventory.removeItem(ItemType.SNOWBALL, 1)
            }
            entitySnowball.spawn()
            player.world?.playSound(player.location, SoundEvent.THROW, -1, "minecraft:player", false, false)
            return true
        }
        return false
    }
}
