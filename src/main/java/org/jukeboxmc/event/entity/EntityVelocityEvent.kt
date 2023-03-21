package org.jukeboxmc.event.entity

import org.jukeboxmc.entity.Entity
import org.jukeboxmc.event.Cancellable
import org.jukeboxmc.math.Vector

/**
 * @author LucGamesYT
 * @version 1.0
 */
class EntityVelocityEvent(entity: Entity, var velocity: Vector) : EntityEvent(entity), Cancellable