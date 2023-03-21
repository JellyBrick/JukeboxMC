package org.jukeboxmc.event.entity

import org.jukeboxmc.entity.Entity
import org.jukeboxmc.event.Cancellable

/**
 * @author LucGamesYT
 * @version 1.0
 */
class EntityHealEvent(entity: Entity, var heal: Float, var cause: Cause) : EntityEvent(entity), Cancellable {

    enum class Cause {
        SATURATION, REGENERATION_EFFECT, HEALING_EFFECT
    }
}