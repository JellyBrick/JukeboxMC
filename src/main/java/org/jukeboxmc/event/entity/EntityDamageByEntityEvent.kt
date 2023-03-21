package org.jukeboxmc.event.entity

import org.jukeboxmc.entity.Entity

/**
 * @author LucGamesYT
 * @version 1.0
 */
class EntityDamageByEntityEvent(entity: Entity, val damager: Entity, damage: Float, damageSource: DamageSource) :
    EntityDamageEvent(entity, damage, damageSource)