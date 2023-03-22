package org.jukeboxmc.event.entity

import org.jukeboxmc.entity.Entity
import org.jukeboxmc.event.Cancellable
import org.jukeboxmc.potion.Effect

/**
 * @author LucGamesYT
 * @version 1.0
 */
class EntityRemoveEffectEvent(entity: Entity, val effect: Effect) : EntityEvent(entity), Cancellable
