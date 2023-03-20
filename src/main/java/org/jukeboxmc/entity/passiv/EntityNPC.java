package org.jukeboxmc.entity.passiv;

import org.jetbrains.annotations.NotNull;
import org.jukeboxmc.entity.EntityLiving;
import org.jukeboxmc.entity.EntityType;
import org.jukeboxmc.util.Identifier;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class EntityNPC extends EntityLiving {

    @Override
    public @NotNull String getName() {
        return "NPC";
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getHeight() {
        return 1.95f;
    }

    @Override
    public @NotNull EntityType getType() {
        return EntityType.NPC;
    }

    @Override
    public Identifier getIdentifier() {
        return Identifier.fromString( "minecraft:npc" );
    }
}
