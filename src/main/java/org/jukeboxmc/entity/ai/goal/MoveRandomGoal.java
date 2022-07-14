package org.jukeboxmc.entity.ai.goal;

import org.jukeboxmc.block.Block;
import org.jukeboxmc.block.BlockFence;
import org.jukeboxmc.block.BlockFenceGate;
import org.jukeboxmc.block.BlockType;
import org.jukeboxmc.block.direction.BlockFace;
import org.jukeboxmc.block.direction.Direction;
import org.jukeboxmc.entity.ai.EntityAI;
import org.jukeboxmc.math.Location;
import org.jukeboxmc.math.Vector;
import org.jukeboxmc.world.chunk.Chunk;

import java.util.Random;

/**
 * @author LucGamesY^T
 * @version 1.0
 */
public class MoveRandomGoal extends Goal {

    private final int radius;
    private final float chance;
    private final Random random;

    private float movementSpeed = 0.60f;
    private int collideCounter = 0;
    private boolean cancel = false;
    private Location targetLocation;

    public MoveRandomGoal( EntityAI entity, int radius, float chance, float movementSpeed ) {
        super( entity );
        this.entity.setOnGround( true );
        this.radius = radius;
        this.chance = chance;
        this.movementSpeed = movementSpeed;
        this.random = new Random();
    }

    public MoveRandomGoal( EntityAI entity, int radius, float chance ) {
        super( entity );
        this.entity.setOnGround( true );
        this.radius = radius;
        this.chance = chance;
        this.random = new Random();
    }

    @Override
    public boolean canStart() {
        return new Random().nextFloat() <= this.chance && !this.entity.isNoAI();
    }

    @Override
    public boolean hasEnded() {
        return new Vector( this.entity.getX(), 0, this.entity.getZ() ).distance( this.targetLocation ) <= 1 || this.cancel || this.entity.isClosed();
    }

    @Override
    public void start() {
        boolean searchLocation = true;
        while ( searchLocation ) {
            float x = this.random.nextFloat() * 2 - 1;
            float z = this.random.nextFloat() * 2 - 1;

            if ( Math.pow( x, 2 ) + Math.pow( z, 2 ) < 1 ) {
                int xx = this.entity.getBlockX() + ( (int) Math.floor( x * radius ) );
                int zz = this.entity.getBlockZ() + ( (int) Math.floor( z * radius ) );
                this.targetLocation = new Location( this.entity.getWorld(), xx, 0, zz, 0, 0 );
                searchLocation = false;
            }
        }
    }

    @Override
    public void tick( long currentTick ) {
        float x = this.targetLocation.getX() - this.entity.getX();
        float z = this.targetLocation.getZ() - this.entity.getZ();
        float diff = Math.abs( x ) + Math.abs( z );
        float motionX = this.movementSpeed * 0.15f * ( x / diff );
        float motionZ = this.movementSpeed * 0.15f * ( z / diff );
        Location nextLocation = this.entity.getLocation().clone();
        nextLocation.setX( this.entity.getX() + motionX );
        nextLocation.setZ( this.entity.getZ() + motionZ );
        nextLocation.setYaw( (float) Math.toDegrees( -Math.atan2( x / diff, z / diff ) ) );

        Location nextBlockLocation = this.entity.getLocation().clone();
        nextBlockLocation.setX( this.entity.getX() + ( motionX * 5 ) );
        nextBlockLocation.setZ( this.entity.getZ() + ( motionZ * 5 ) );
        Block nextBlock = nextBlockLocation.getBlock();

        if ( nextBlock instanceof BlockFence || nextBlock instanceof BlockFenceGate ) {
            Direction opposite = this.entity.getDirection().opposite();
            Location location = this.entity.getLocation().clone().getSide( opposite, this.radius * 2 );
            location.setY( 0 );
            if ( this.collideCounter > 2 ) {
                this.cancel = true;
                return;
            }
            this.targetLocation = location;
            this.collideCounter++;
            return;
        }

        Block nextBlockAbove = nextBlock.getSide( BlockFace.UP );
        Block nextBlockAboveAbove = nextBlock.getSide( BlockFace.UP ).getSide( BlockFace.UP );

        if ( nextBlock.isSolid() && this.entity.getLocation().getSide( BlockFace.UP ).getBlock().isSolid() ) {
            this.cancel = true;
            return;
        }
        if ( nextBlock.isSolid() && nextBlockAbove.isSolid() ) {
            this.cancel = true;
            return;
        }
        if ( nextBlock.isSolid() && nextBlockAbove.isTransparent() && nextBlockAboveAbove.isSolid() && this.entity.getHeight() > 1.0f ) {
            this.cancel = true;
            return;
        }
        if ( nextBlock.isSolid() && !nextBlockAbove.isTransparent() ) {
            if ( nextBlockAboveAbove.isTransparent() && this.entity.getHeight() > 1.0f ) {
                this.cancel = true;
                return;
            }
        }
        if ( nextBlock.isSolid() ) {
            nextLocation.setY( this.entity.getY() + 1f );
        }

        Block blockForwardDown = nextBlockLocation.clone().subtract( 0, 1, 0 ).getBlock();
        if ( !blockForwardDown.isSolid() && nextBlock.getType().equals( BlockType.AIR ) ) {
            nextLocation.setY( this.entity.getY() - 1f );
        }

        this.entity.setLocation( nextLocation );

        Chunk fromChunk = this.entity.getLastLocation().getChunk();
        Chunk toChunk = this.entity.getChunk();
        if ( toChunk.getX() != fromChunk.getX() || toChunk.getZ() != fromChunk.getZ() ) {
            fromChunk.removeEntity( this.entity );
            toChunk.addEntity( this.entity );
        }
        this.entity.updateAbsoluteMovement();
        this.entity.setOnGround( this.entity.getY() - this.entity.getLastLocation().getY() <= 0 );
    }

    @Override
    public void end() {
        this.cancel = false;
        this.collideCounter = 0;
        this.targetLocation = null;
    }
}
