package org.jukeboxmc.math;

import org.jukeboxmc.block.Block;
import org.jukeboxmc.block.direction.BlockFace;
import org.jukeboxmc.block.direction.Direction;
import org.jukeboxmc.world.Biome;
import org.jukeboxmc.world.Dimension;
import org.jukeboxmc.world.World;
import org.jukeboxmc.world.chunk.Chunk;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class Location extends Vector {

    private World world;
    private float yaw;
    private float pitch;

    public Location( World world, float x, float y, float z, float yaw, float pitch, Dimension dimension ) {
        super( x, y, z );
        this.world = world;
        this.yaw = yaw;
        this.pitch = pitch;
        this.dimension = dimension;
    }

    public Location( World world, float x, float y, float z, float yaw, float pitch ) {
        this( world, x, y, z, yaw, pitch, Dimension.OVERWORLD );
    }

    public Location( World world, float x, float y, float z, Dimension dimension ) {
        this( world, x, y, z, 0, 0, dimension );
    }

    public Location( World world, float x, float y, float z ) {
        this( world, x, y, z, 0, 0, Dimension.OVERWORLD );
    }

    public Location( World world, int x, int y, int z, Dimension dimension ) {
        this( world, x, y, z, 0, 0, dimension );
    }

    public Location( World world, int x, int y, int z ) {
        this( world, x, y, z, 0, 0, Dimension.OVERWORLD );
    }

    public Location( World world, Vector vector, float yaw, float pitch, Dimension dimension ) {
        this( world, vector.getX(), vector.getY(), vector.getZ(), yaw, pitch, dimension );
    }

    public Location( World world, Vector vector, float yaw, float pitch ) {
        this( world, vector.getX(), vector.getY(), vector.getZ(), yaw, pitch, Dimension.OVERWORLD );
    }

    public Location( World world, Vector vector ) {
        this( world, vector.getX(), vector.getY(), vector.getZ(), 0, 0, Dimension.OVERWORLD );
    }

    @Override
    public Location add( float x, float y, float z ) {
        return new Location( this.world, this.x + x, this.y + y, this.z + z, this.yaw, this.pitch, this.dimension );
    }

    @Override
    public Location add( Vector vector ) {
        return new Location( this.world, this.x + vector.x, this.y + vector.y, this.z + vector.z, this.yaw, this.pitch, this.dimension );
    }

    @Override
    public Location subtract( float x, float y, float z ) {
        return new Location( this.world, this.x - x, this.y - y, this.z - z, this.yaw, this.pitch, this.dimension );
    }

    @Override
    public Location subtract( Vector vector ) {
        return new Location( this.world, this.x - vector.x, this.y - vector.y, this.z - vector.z, this.yaw, this.pitch, this.dimension );
    }

    @Override
    public Location multiply( float x, float y, float z ) {
        return new Location( this.world, this.x * x, this.y * y, this.z * z, this.yaw, this.pitch, this.dimension );
    }

    @Override
    public Location multiply( Vector vector ) {
        return new Location( this.world, this.x * vector.x, this.y * vector.y, this.z * vector.z, this.yaw, this.pitch, this.dimension );
    }

    @Override
    public Location divide( float x, float y, float z ) {
        return new Location( this.world, this.x / x, this.y / y, this.z / z, this.yaw, this.pitch, this.dimension );
    }

    @Override
    public Location divide( Vector vector ) {
        return new Location( this.world, this.x / vector.x, this.y / vector.y, this.z / vector.z, this.yaw, this.pitch, this.dimension );
    }

    public Location getSide( Direction direction ) {
        return switch ( direction ) {
            case SOUTH -> this.getRelative( Vector.south() );
            case NORTH -> this.getRelative( Vector.north() );
            case EAST -> this.getRelative( Vector.east() );
            case WEST -> this.getRelative( Vector.west() );
        };
    }

    public Location getSide( Direction direction, int step ) {
        return switch ( direction ) {
            case SOUTH -> this.getRelative( Vector.south( step ) );
            case NORTH -> this.getRelative( Vector.north( step ) );
            case EAST -> this.getRelative( Vector.east( step ) );
            case WEST -> this.getRelative( Vector.west( step ) );
        };
    }

    public Location getSide( BlockFace blockFace ) {
        return switch ( blockFace ) {
            case DOWN -> this.getRelative( Vector.down() );
            case UP -> this.getRelative( Vector.up() );
            case SOUTH -> this.getRelative( Vector.south() );
            case NORTH -> this.getRelative( Vector.north() );
            case EAST -> this.getRelative( Vector.east() );
            case WEST -> this.getRelative( Vector.west() );
        };
    }

    public Location getSide( BlockFace blockFace, int step ) {
        return switch ( blockFace ) {
            case DOWN -> this.getRelative( Vector.down(step) );
            case UP -> this.getRelative( Vector.up(step) );
            case SOUTH -> this.getRelative( Vector.south(step) );
            case NORTH -> this.getRelative( Vector.north(step) );
            case EAST -> this.getRelative( Vector.east(step) );
            case WEST -> this.getRelative( Vector.west(step) );
        };
    }

    public Location getRelative( Vector position ) {
        float x = this.getX() + position.getBlockX();
        float y = this.getY() + position.getBlockY();
        float z = this.getZ() + position.getBlockZ();
        return new Location( this.world, x, y, z, this.yaw, this.pitch, this.dimension );
    }

    public World getWorld() {
        return this.world;
    }

    public void setWorld( World world ) {
        this.world = world;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw( float yaw ) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch( float pitch ) {
        this.pitch = pitch;
    }

    public Block getBlock() {
        return this.world.getBlock( this );
    }

    public Biome getBiome() {
        return this.world.getBiome( this.getBlockX(), this.getBlockY(), this.getBlockZ() );
    }

    public Block getBlock( int layer ) {
        return this.world.getBlock( this, layer );
    }

    public Chunk getChunk() {
        return this.world.getChunk( this.getBlockX() >> 4, this.getBlockZ() >> 4, this.dimension );
    }

    public Vector getDirection() {
        double pitch = ( ( this.pitch + 90 ) * Math.PI ) / 180;
        double yaw = ( ( this.yaw + 90 ) * Math.PI ) / 180;
        double x = Math.sin( pitch ) * Math.cos( yaw );
        double z = Math.sin( pitch ) * Math.sin( yaw );
        double y = Math.cos( pitch );
        return new Vector( (float) x, (float) y, (float) z ).normalize();
    }

    @Override
    public Location clone() {
        Location clone = (Location) super.clone();
        clone.world = this.world;
        clone.yaw = this.yaw;
        clone.pitch = this.pitch;
        return clone;
    }

    @Override
    public String toString() {
        return "Location{" +
                "world=" + this.world.getName() +
                ", x=" + this.x +
                ", y=" + this.y +
                ", z=" + this.z +
                ", yaw=" + this.yaw +
                ", pitch=" + this.pitch +
                ", dimension=" + this.dimension.name() +
                '}';
    }
}
