package org.jukeboxmc.event.entity

import org.jukeboxmc.entity.Entity
import org.jukeboxmc.event.Cancellable

/**
 * @author LucGamesYT
 * @version 1.0
 */
class EntitySpawnEvent(entity: Entity) : EntityEvent(entity), Cancellable