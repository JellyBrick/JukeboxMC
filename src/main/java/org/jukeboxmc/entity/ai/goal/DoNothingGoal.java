package org.jukeboxmc.entity.ai.goal;

import org.jukeboxmc.entity.Entity;

import java.util.Random;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class DoNothingGoal extends Goal {

    private final int minSeconds;
    private final int maxSeconds;
    private final float chance;

    private int ticks;

    public DoNothingGoal( Entity entity, int minSeconds, int maxSeconds, float chance ) {
        super( entity );
        this.minSeconds = minSeconds;
        this.maxSeconds = maxSeconds;
        this.chance = chance;
    }

    @Override
    public boolean canStart() {
        return new Random().nextFloat() <= this.chance;
    }

    @Override
    public boolean hasEnded() {
        return this.ticks <= 0;
    }

    @Override
    public void start() {
        this.ticks = new Random().nextInt( this.minSeconds, this.maxSeconds ) * 20;
    }

    @Override
    public void tick(long currentTick) {
        this.ticks--;
    }

    @Override
    public void end() {

    }

}
