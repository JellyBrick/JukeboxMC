package org.jukeboxmc.entity.ai.goal;

import org.jukeboxmc.block.Block;
import org.jukeboxmc.block.BlockFence;
import org.jukeboxmc.block.BlockType;
import org.jukeboxmc.entity.Entity;
import org.jukeboxmc.math.Location;
import org.jukeboxmc.math.Vector;
import org.jukeboxmc.util.PerformanceCheck;
import org.jukeboxmc.world.Dimension;
import org.jukeboxmc.world.chunk.Chunk;

import java.util.Random;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class MoveRandomGoal extends Goal {

    private final int radius;
    private final float chance;

    private boolean cancel = false;
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
        return new Vector( this.entity.getX(), 0, this.entity.getZ() ).distance( this.targetLocation ) <= 1 || this.cancel;
    }

    @Override
    public void start() {
        int randomX = new Random().nextInt( this.entity.getBlockX() - this.radius, this.entity.getBlockX() + this.radius );
        int randomZ = new Random().nextInt( this.entity.getBlockZ() - this.radius, this.entity.getBlockZ() + this.radius );
        this.targetLocation = new Location( this.entity.getWorld(), randomX, 0, randomZ, 0, 0 );
    }

    @Override
    public void tick( long currentTick ) {
        float movementSpeed = 0.75f;
        float x = this.targetLocation.getX() - this.entity.getX();
        float z = this.targetLocation.getZ() - this.entity.getZ();
        float diff = Math.abs( x ) + Math.abs( z );

        float motionX = movementSpeed * 0.15f * ( x / diff );
        float motionZ = movementSpeed * 0.15f * ( z / diff );

        Location nextLocation = this.entity.getLocation().clone();
        nextLocation.setX( this.entity.getX() + motionX );
        nextLocation.setZ( this.entity.getZ() + motionZ );
        nextLocation.setYaw( (float) Math.toDegrees( -Math.atan2( x / diff, z / diff ) ) );

        Location nextBlockLocation = this.entity.getLocation().clone();
        nextBlockLocation.setX( this.entity.getX() + ( motionX * 5 ) );
        nextBlockLocation.setZ( this.entity.getZ() + ( motionZ * 5 ) );

        if ( nextBlockLocation.getBlock() instanceof BlockFence ) {
            this.cancel = true;
            return;
        }

        if ( nextBlockLocation.getBlock().isSolid() ) {
            nextLocation.setY( this.entity.getY() + 1f );
        }

        Block blockForwardDown = nextBlockLocation.clone().subtract( 0, 1, 0 ).getBlock();
        if ( !blockForwardDown.isSolid() && nextBlockLocation.getBlock().getType().equals( BlockType.AIR ) ) {
            nextLocation.setY( this.entity.getY() - 1f );
        }

        this.entity.setLocation( nextLocation );
        this.entity.updateMovement();
    }


    @Override
    public void end() {
        this.cancel = false;
        this.targetLocation = null;
    }

    private Location findNextBetterLocation() {
        while ( true ) {
            int randomX = new Random().nextInt( this.entity.getBlockX() - this.radius, this.entity.getBlockX() + this.radius );
            int randomZ = new Random().nextInt( this.entity.getBlockZ() - this.radius, this.entity.getBlockZ() + this.radius );
            Chunk chunk = this.entity.getWorld().getChunk( randomX >> 4, randomZ >> 4, this.entity.getDimension() );
            int blockY = chunk.getHeighestBlockAt( randomX, randomZ ).getLocation().getBlockY() + 1;

            for ( int x = randomX - this.radius; x <= randomX + this.radius; x++ ) {
                for ( int z = randomZ - this.radius; z <= randomZ + this.radius; z++ ) {
                    Block block = this.entity.getWorld().getBlock( x, blockY, z, 0 );
                    if ( block instanceof BlockFence ) {
                        if ( block.getLocation().distance( new Vector( randomX, blockY, randomZ ) ) >= radius  ) {
                            return new Location( this.entity.getWorld(), randomX, 0, randomZ );
                        }
                    }
                }
            }
        }
    }
}
