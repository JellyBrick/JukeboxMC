package org.jukeboxmc.entity

import org.jukeboxmc.entity.item.EntityItem
import org.jukeboxmc.entity.passiv.EntityFallingBlock
import org.jukeboxmc.entity.passiv.EntityHuman
import org.jukeboxmc.entity.passiv.EntityNPC
import org.jukeboxmc.entity.projectile.EntityArrow
import org.jukeboxmc.entity.projectile.EntityEgg
import org.jukeboxmc.entity.projectile.EntityFishingHook
import org.jukeboxmc.entity.projectile.EntitySnowball

/**
 * @author LucGamesYT
 * @version 1.0
 */
object EntityRegistry {
    private val ENTITYCLASS_FROM_ENTITYTYPE: MutableMap<EntityType?, Class<out Entity>> = LinkedHashMap()
    fun init() {
        register(EntityType.HUMAN, EntityHuman::class.java)
        register(EntityType.ITEM, EntityItem::class.java)
        register(EntityType.NPC, EntityNPC::class.java)
        register(EntityType.ARROW, EntityArrow::class.java)
        register(EntityType.FISHING_HOOK, EntityFishingHook::class.java)
        register(EntityType.SNOWBALL, EntitySnowball::class.java)
        register(EntityType.EGG, EntityEgg::class.java)
        register(EntityType.FALLING_BLOCK, EntityFallingBlock::class.java)
    }

    private fun register(entityType: EntityType, entityClazz: Class<out Entity>) {
        ENTITYCLASS_FROM_ENTITYTYPE[entityType] = entityClazz
    }

    fun getEntityClass(entityType: EntityType?): Class<out Entity> {
        return ENTITYCLASS_FROM_ENTITYTYPE[entityType]!!
    }
}
