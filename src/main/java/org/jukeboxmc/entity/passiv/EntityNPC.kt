package org.jukeboxmc.entity.passiv

import org.jukeboxmc.entity.EntityType
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class EntityNPC : EntityLiving() {
    val name: String
        get() = "NPC"
    val width: Float
        get() = 0.6f
    val height: Float
        get() = 1.95f
    val type: EntityType
        get() = EntityType.NPC
    val identifier: Identifier
        get() = Identifier.Companion.fromString("minecraft:npc")
}