package org.jukeboxmc.event.entity

import org.jukeboxmc.entity.Entity
import org.jukeboxmc.entity.projectile.EntityProjectile
import org.jukeboxmc.event.Cancellable

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ProjectileHitEntityEvent(entity: Entity, val projectile: EntityProjectile) : EntityEvent(entity), Cancellable
