package org.jukeboxmc.entity.ai.goal;

import org.jukeboxmc.entity.Entity;
import org.jukeboxmc.math.Location;

import java.util.Random;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class LookAtRandomGoal extends Goal {

    private final int minSeconds;
    private final int maxSeconds;
    private final float chance;

    private int ticks;

    public LookAtRandomGoal( Entity entity, int minSeconds, int maxSeconds, float chance ) {
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

        int randomX = new Random().nextInt( -1, 1 );
        int randomZ = new Random().nextInt( -1, 1 );

        float x = ( this.entity.getX() + randomX ) - this.entity.getX();
        float z = ( this.entity.getZ() + randomZ ) - this.entity.getZ();
        float diff = Math.abs( x ) + Math.abs( z );

        Location location = this.entity.getLocation().clone();

        location.setYaw( (float) Math.toDegrees( -Math.atan2( x / diff, z / diff ) ) );
        this.entity.sendEntityMovePacket( location.add( 0, this.entity.getEyeHeight(), 0 ), true );
    }

    @Override
    public void tick() {
        this.ticks--;
    }

    @Override
    public void end() {

    }

}
