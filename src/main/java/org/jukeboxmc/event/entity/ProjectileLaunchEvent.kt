package org.jukeboxmc.event.entity

import org.jukeboxmc.entity.Entity
import org.jukeboxmc.entity.projectile.EntityProjectile
import org.jukeboxmc.event.Cancellable

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ProjectileLaunchEvent(entity: Entity, val cause: Cause) : EntityEvent(entity), Cancellable {

    override fun getEntity(): Entity {
        return super.getEntity() as EntityProjectile
    }

    enum class Cause {
        BOW, CROSSBOW, EXP_BOTTLE, FISHING_ROD, ENDER_PEARL, SNOWBALL, EGG
    }
}