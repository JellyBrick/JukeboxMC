package org.jukeboxmc.entity.passiv

import org.jukeboxmc.entity.EntityLiving
import org.jukeboxmc.entity.EntityType
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class EntityNPC : EntityLiving() {
    override val name: String
        get() = "NPC"
    override val width: Float
        get() = 0.6f
    override val height: Float
        get() = 1.95f
    override val type: EntityType
        get() = EntityType.NPC
    override val identifier: Identifier
        get() = Identifier.fromString("minecraft:npc")
}
