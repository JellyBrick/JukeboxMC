package org.jukeboxmc.entity.projectile

import java.util.concurrent.TimeUnit
import org.jukeboxmc.entity.Entity
import org.jukeboxmc.entity.EntityType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.math.Location
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.Particle

/**
 * @author LucGamesYT
 * @version 1.0
 */
class EntityEgg : EntityProjectile() {
    override fun update(currentTick: Long) {
        if (this.isClosed) {
            return
        }
        super.update(currentTick)
        if (age > TimeUnit.MINUTES.toMillis(5) || isCollided) {
            close()
            spawnEggParticle(location)
        }
    }

    override fun onCollidedWithEntity(entity: Entity?) {
        close()
    }

    val name: String
        get() = "Egg"
    val width: Float
        get() = 0.25f
    val height: Float
        get() = 0.25f
    val type: EntityType
        get() = EntityType.EGG
    val identifier: Identifier
        get() = Identifier.Companion.fromString("minecraft:egg")

    private fun spawnEggParticle(location: Location) {
        val itemEgg: Item = Item.Companion.create<Item>(ItemType.EGG)
        for (i in 0..5) {
            this.world.spawnParticle(
                Particle.ITEM_BREAK,
                location.add(0f, 0.5f, 0f),
                itemEgg.runtimeId shl 16 or itemEgg.meta
            )
        }
    }
}