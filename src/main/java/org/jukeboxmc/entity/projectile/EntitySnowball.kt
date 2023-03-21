package org.jukeboxmc.entity.projectile

import java.util.concurrent.TimeUnit
import org.jukeboxmc.entity.Entity
import org.jukeboxmc.entity.EntityType
import org.jukeboxmc.math.Location
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.Particle

/**
 * @author LucGamesYT
 * @version 1.0
 */
class EntitySnowball : EntityProjectile() {
    override fun update(currentTick: Long) {
        if (this.isClosed) {
            return
        }
        super.update(currentTick)
        if (age > TimeUnit.MINUTES.toMillis(5) || isCollided) {
            close()
            spawnSnowballParticle(location)
        }
    }

    override fun onCollidedWithEntity(entity: Entity?) {
        close()
    }

    val name: String
        get() = "Snowball"
    val width: Float
        get() = 0.25f
    val height: Float
        get() = 0.25f
    val drag: Float
        get() = 0.01f
    val gravity: Float
        get() = 0.03f
    val type: EntityType
        get() = EntityType.SNOWBALL
    val identifier: Identifier
        get() = Identifier.Companion.fromString("minecraft:snowball")

    private fun spawnSnowballParticle(location: Location) {
        for (i in 0..5) {
            this.world.spawnParticle(Particle.SNOWBALL_POOF, location.add(0f, 0.5f, 0f))
        }
    }
}