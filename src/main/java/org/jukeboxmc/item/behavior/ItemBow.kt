package org.jukeboxmc.item.behavior

import org.jukeboxmc.Server
import org.jukeboxmc.entity.Entity
import org.jukeboxmc.entity.EntityType
import org.jukeboxmc.entity.projectile.EntityArrow
import org.jukeboxmc.event.entity.ProjectileLaunchEvent
import org.jukeboxmc.item.Burnable
import org.jukeboxmc.item.Durability
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.enchantment.EnchantmentInfinity
import org.jukeboxmc.item.enchantment.EnchantmentType
import org.jukeboxmc.math.Location
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.GameMode
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.Sound
import java.time.Duration
import java.util.Objects
import java.util.concurrent.TimeUnit
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemBow : Item, Durability, Burnable {
    constructor(identifier: Identifier) : super(identifier)
    constructor(itemType: ItemType) : super(itemType)

    override fun useInAir(player: Player, clickVector: Vector): Boolean {
        player.setAction(true)
        return super.useInAir(player, clickVector)
    }

    override val burnTime: Duration?
        get() = Duration.ofMillis(300)
    override val maxDurability: Int
        get() = 384

    fun shoot(player: Player) {
        if (player.actionStart == -1L) {
            return
        }
        val tick: Float = (Server.instance.currentTick - player.actionStart).toFloat() / 20
        val force = min((tick * tick + tick * 2) / 3, 1f) * 2
        player.setAction(false)
        if (!player.inventory.contains(create(ItemType.ARROW)) && player.gameMode != GameMode.CREATIVE) {
            return
        }
        var powerModifier = 0
        val power = getEnchantment(EnchantmentType.POWER)
        if (power != null) {
            powerModifier = power.level.toInt()
        }
        var punchModifier = 0
        val punch = getEnchantment(EnchantmentType.PUNCH)
        if (punch != null) {
            punchModifier = punch.level.toInt()
        }
        var flameModifier = 0
        val flame = getEnchantment(EnchantmentType.FLAME)
        if (flame != null) {
            flameModifier = flame.level.toInt()
        }
        val arrow = Objects.requireNonNull<EntityArrow>(Entity.create<EntityArrow>(EntityType.ARROW))
        arrow.shooter = player
        arrow.location = Location(
            player.world,
            player.x,
            player.y + player.eyeHeight,
            player.z,
            (if (player.yaw > 180) 360 else 0) - player.yaw,
            -player.pitch,
        )
        arrow.setVelocity(
            Vector(
                (-sin(player.yaw / 180 * Math.PI) * cos(player.pitch / 180 * Math.PI)).toFloat(),
                (-sin(player.pitch / 180 * Math.PI)).toFloat(),
                (cos(player.yaw / 180 * Math.PI) * cos(player.pitch / 180 * Math.PI)).toFloat(),
            ).multiply(force, force, force),
            false,
        )
        arrow.flameModifier = flameModifier
        arrow.punchModifier = punchModifier
        arrow.powerModifier = powerModifier
        arrow.setPickupDelay(500, TimeUnit.MILLISECONDS)
        arrow.force = force
        val event = ProjectileLaunchEvent(arrow, ProjectileLaunchEvent.Cause.BOW)
        player.world.server?.pluginManager?.callEvent(event)
        if (!event.isCancelled) {
            val enchantmentInfinity = getEnchantment(EnchantmentType.INFINITY) as EnchantmentInfinity
            arrow.setWasInfinityArrow(true)
            arrow.spawn()
            arrow.isBurning = flameModifier > 0
            player.playSound(Sound.RANDOM_BOW, 1f, 1f)
        }
    }
}
