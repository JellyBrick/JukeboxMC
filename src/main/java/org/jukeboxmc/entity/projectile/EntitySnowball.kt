package org.jukeboxmc.entity.projectile

import org.jukeboxmc.entity.Entity
import org.jukeboxmc.entity.EntityType
import org.jukeboxmc.math.Location
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.Particle
import java.util.concurrent.TimeUnit

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

    override val name: String
        get() = "Snowball"
    override val width: Float
        get() = 0.25f
    override val height: Float
        get() = 0.25f
    override var drag: Float = 0.01f
    override var gravity: Float = 0.03f
    override val type: EntityType
        get() = EntityType.SNOWBALL
    override val identifier: Identifier
        get() = Identifier.fromString("minecraft:snowball")

    private fun spawnSnowballParticle(location: Location) {
        for (i in 0..5) {
            this.world?.spawnParticle(Particle.SNOWBALL_POOF, location.add(0f, 0.5f, 0f))
        }
    }
}
