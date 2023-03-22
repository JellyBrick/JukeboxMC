package org.jukeboxmc.event.entity

import org.jukeboxmc.entity.Entity
import org.jukeboxmc.event.Event

/**
 * @author LucGamesYT
 * @version 1.0
 */
abstract class EntityEvent(private var entity: Entity) : Event() {
    open fun getEntity(): Entity {
        return entity
    }

    fun setEntity(entity: Entity) {
        this.entity = entity
    }
}
