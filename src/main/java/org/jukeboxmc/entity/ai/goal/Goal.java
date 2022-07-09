package org.jukeboxmc.entity.ai.goal;

import org.jukeboxmc.entity.ai.EntityAI;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public abstract class Goal {

    protected final EntityAI entity;

    protected Goal( EntityAI entity ) {
        this.entity = entity;
    }

    public abstract boolean canStart();

    public abstract boolean hasEnded();

    public abstract void start();

    public abstract void tick(long currentTick);

    public abstract void end();

}
