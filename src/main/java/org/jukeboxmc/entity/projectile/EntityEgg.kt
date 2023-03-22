package org.jukeboxmc.entity.projectile

import org.jukeboxmc.entity.Entity
import org.jukeboxmc.entity.EntityType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.math.Location
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.Particle
import java.util.concurrent.TimeUnit

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

    override fun onCollidedWithEntity(entity: Entity) {
        close()
    }

    override val name: String
        get() = "Egg"
    override val width: Float
        get() = 0.25f
    override val height: Float
        get() = 0.25f
    override val type: EntityType
        get() = EntityType.EGG
    override val identifier: Identifier
        get() = Identifier.fromString("minecraft:egg")

    private fun spawnEggParticle(location: Location) {
        val itemEgg: Item = Item.create<Item>(ItemType.EGG)
        for (i in 0..5) {
            this.world?.spawnParticle(
                Particle.ITEM_BREAK,
                location.add(0f, 0.5f, 0f),
                itemEgg.runtimeId shl 16 or itemEgg.meta,
            )
        }
    }
}
