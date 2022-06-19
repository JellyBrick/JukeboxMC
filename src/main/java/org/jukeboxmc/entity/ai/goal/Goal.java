package org.jukeboxmc.entity.ai.goal;

import org.jukeboxmc.entity.Entity;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public abstract class Goal {

    protected final Entity entity;

    protected Goal( Entity entity ) {
        this.entity = entity;
    }

    public abstract boolean canStart();

    public abstract boolean hasEnded();

    public abstract void start();

    public abstract void tick();

    public abstract void end();

}
