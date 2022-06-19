package org.jukeboxmc.entity.ai.goal;

import org.jukeboxmc.entity.Entity;
import org.jukeboxmc.math.Location;
import org.jukeboxmc.math.Vector;

import java.util.Random;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class MoveRandomGoal extends Goal {

    private final int radius;
    private final float chance;

    private Location targetLocation;

    public MoveRandomGoal( Entity entity, int radius, float chance ) {
        super( entity );

        this.radius = radius;
        this.chance = chance;
    }

    @Override
    public boolean canStart() {
        return new Random().nextFloat() <= this.chance;
    }

    @Override
    public boolean hasEnded() {
        return this.entity.getLocation().distance( this.targetLocation ) <= 1;
    }

    @Override
    public void start() {
        int randomX = new Random().nextInt( this.entity.getBlockX() - radius, this.entity.getBlockX() + radius );
        int randomZ = new Random().nextInt( this.entity.getBlockZ() - radius, this.entity.getBlockZ() + radius );
        this.targetLocation = new Location( this.entity.getWorld(), randomX, 4, randomZ, 0, 0 );
    }

    @Override
    public void tick() {
        float movementSpeed = 0.7f;
        float x = this.targetLocation.getX() - this.entity.getX();
        float z = this.targetLocation.getZ() - this.entity.getZ();
        float diff = Math.abs( x ) + Math.abs( z );

        float motionX = movementSpeed * 0.15f * ( x / diff );
        float motionZ = movementSpeed * 0.15f * ( z / diff );

        Location nextLocation = this.entity.getLocation().clone();
        nextLocation.setX( this.entity.getX() + motionX );
        nextLocation.setZ( this.entity.getZ() + motionZ );
        nextLocation.setYaw( (float) Math.toDegrees( -Math.atan2( x / diff, z / diff ) ) );

        this.entity.setLocation( nextLocation );
        this.entity.sendEntityMovePacket( nextLocation.add( 0, this.entity.getEyeHeight(), 0 ), true );
    }

    @Override
    public void end() {
        this.targetLocation = null;
    }

}
