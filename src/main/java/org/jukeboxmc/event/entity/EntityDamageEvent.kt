package org.jukeboxmc.event.entity

import org.jukeboxmc.entity.Entity
import org.jukeboxmc.event.Cancellable

/**
 * @author LucGamesYT
 * @version 1.0
 */
open class EntityDamageEvent(entity: Entity, var damage: Float, val damageSource: DamageSource) :
    EntityEvent(entity),
    Cancellable {
    var finalDamage = 0f

    enum class DamageSource {
        ENTITY_ATTACK, FALL, VOID, PROJECTILE, DROWNING, CACTUS, LAVA, ON_FIRE, FIRE, ENTITY_EXPLODE, MAGIC_EFFECT, STARVE, API, COMMAND
    }
}
