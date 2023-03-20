package org.jukeboxmc.math;

import com.nukkitx.math.vector.Vector3d;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.math.vector.Vector3l;
import lombok.SneakyThrows;
import org.apache.commons.math3.util.FastMath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jukeboxmc.world.Dimension;

import java.util.Objects;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class Vector implements Cloneable {

    protected float x;
    protected float y;
    protected float z;
    protected Dimension dimension = Dimension.OVERWORLD;

    public Vector( float x, float y, float z, Dimension dimension ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
    }

    public Vector( int x, int y, int z, Dimension dimension ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
    }

    public Vector( float x, float y, float z ) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector( int x, int y, int z ) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(@NotNull Vector3f vector3f ) {
        this.x = vector3f.getX();
        this.y = vector3f.getY();
        this.z = vector3f.getZ();
    }

    public Vector(@NotNull Vector3i vector3i ) {
        this.x = vector3i.getX();
        this.y = vector3i.getY();
        this.z = vector3i.getZ();
    }

    public static @NotNull Vector zero() {
        return new Vector( 0, 0, 0 );
    }

    public @NotNull Vector up() {
        return new Vector( this.x, this.y + 1, this.z );
    }

    public @NotNull Vector down() {
        return new Vector( this.x, this.y - 1, this.z );
    }

    public @NotNull Vector north() {
        return new Vector( this.x, this.y, this.z -1 );
    }

    public @NotNull Vector east() {
        return new Vector( this.x + 1, this.y, this.z );
    }

    public @NotNull Vector south() {
        return new Vector( this.x, this.y, this.z + 1 );
    }

    public @NotNull Vector west() {
        return new Vector( this.x -1, this.y, this.z );
    }

    public void setVector( int x, int y, int z ) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }

    public void setX( final float x ) {
        this.x = x;
    }

    public void setY( final float y ) {
        this.y = y;
    }

    public void setZ( final float z ) {
        this.z = z;
    }

    public int getBlockX() {
        return (int) FastMath.floor( this.x );
    }

    public int getBlockY() {
        return (int) FastMath.floor( this.y );
    }

    public int getBlockZ() {
        return (int) FastMath.floor( this.z );
    }

    public int getChunkX() {
        return this.getBlockX() >> 4;
    }

    public int getChunkZ() {
        return this.getBlockZ() >> 4;
    }

    public Dimension getDimension() {
        return this.dimension;
    }

    public void setDimension( Dimension dimension ) {
        this.dimension = dimension;
    }

    public @Nullable Vector getVectorWhenXIsOnLine(@NotNull Vector other, float x ) {
        float xDiff = other.x - this.x;
        float yDiff = other.y - this.y;
        float zDiff = other.z - this.z;

        float f = ( x - this.x ) / xDiff;
        return ( f >= 0F && f <= 1F ) ? new Vector( this.x + xDiff * f, this.y + yDiff * f, this.z + zDiff * f, this.dimension ) : null;
    }

    public @Nullable Vector getVectorWhenYIsOnLine(@NotNull Vector other, float y ) {
        float xDiff = other.x - this.x;
        float yDiff = other.y - this.y;
        float zDiff = other.z - this.z;

        float f = ( y - this.y ) / yDiff;
        return ( f >= 0F && f <= 1F ) ? new Vector( this.x + xDiff * f, this.y + yDiff * f, this.z + zDiff * f, this.dimension ) : null;
    }

    public @Nullable Vector getVectorWhenZIsOnLine(@NotNull Vector other, float z ) {
        float xDiff = other.x - this.x;
        float yDiff = other.y - this.y;
        float zDiff = other.z - this.z;

        float f = ( z - this.z ) / zDiff;
        return ( f >= 0F && f <= 1F ) ? new Vector( this.x + xDiff * f, this.y + yDiff * f, this.z + zDiff * f, this.dimension ) : null;
    }

    public Vector add( final float x, final float y, final float z ) {
        return new Vector( this.x + x, this.y + y, this.z + z, this.dimension );
    }

    public Vector subtract( final float x, final float y, final float z ) {
        return new Vector( this.x - x, this.y - y, this.z - z, this.dimension );
    }

    public Vector multiply( final float x, final float y, final float z ) {
        return new Vector( this.x * x, this.y * y, this.z * z, this.dimension );
    }

    public Vector divide( final float x, final float y, final float z ) {
        return new Vector( this.x / x, this.y / y, this.z / z, this.dimension );
    }

    public Vector normalize() {
        final float squaredLength = this.squaredLength();
        return this.divide( squaredLength, squaredLength, squaredLength );
    }

    public float distance( final @NotNull Vector vector ) {
        return (float) Math.sqrt( this.distanceSquared( vector ) );
    }

    public float distanceSquared( final @NotNull Vector vector ) {
        return (float) ( FastMath.pow( ( this.x - vector.getX() ), 2 ) + FastMath.pow( ( this.y - vector.getY() ), 2 ) +
                FastMath.pow( ( this.z - vector.getZ() ), 2 ) );
    }

    public float squaredLength() {
        return (float) ( FastMath.sqrt( this.x * this.x + this.y * this.y + this.z * this.z ) );
    }

    public @NotNull Vector3f toVector3f() {
        return Vector3f.from( this.x, this.y, this.z );
    }

    public @NotNull Vector3d toVector3D() {
        return Vector3d.from( this.x, this.y, this.z );
    }

    public @NotNull Vector3l toVector3l() {
        return Vector3l.from( this.x, this.y, this.z );
    }

    public @NotNull Vector3i toVector3i() {
        return Vector3i.from( this.x, this.y, this.z );
    }

    @Override
    public boolean equals(@Nullable Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        Vector vector = (Vector) o;
        return Float.compare( vector.x, this.x ) == 0 && Float.compare( vector.y, this.y ) == 0 && Float.compare( vector.z, this.z ) == 0 && this.dimension == vector.dimension;
    }

    @Override
    public int hashCode() {
        return Objects.hash( this.x, this.y, this.z, this.dimension );
    }

    @SneakyThrows
    @Override
    public Vector clone() {
        Vector clone = (Vector) super.clone();
        clone.x = this.x;
        clone.y = this.y;
        clone.z = this.z;
        return clone;
    }

    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", dimension=" + dimension +
                '}';
    }

    public @NotNull Vector round() {
        return new Vector( Math.round( this.x ), Math.round( this.y ), Math.round( this.z ) );
    }
}
