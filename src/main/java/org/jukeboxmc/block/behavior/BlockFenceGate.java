package org.jukeboxmc.block.behavior;

import com.nukkitx.nbt.NbtMap;
import com.nukkitx.protocol.bedrock.data.LevelEventType;
import org.jetbrains.annotations.NotNull;
import org.jukeboxmc.block.Block;
import org.jukeboxmc.block.direction.BlockFace;
import org.jukeboxmc.block.direction.Direction;
import org.jukeboxmc.item.Item;
import org.jukeboxmc.math.Vector;
import org.jukeboxmc.player.Player;
import org.jukeboxmc.util.Identifier;
import org.jukeboxmc.world.World;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class BlockFenceGate extends Block {

    public BlockFenceGate( Identifier identifier ) {
        super( identifier );
    }

    public BlockFenceGate( Identifier identifier, NbtMap blockStates ) {
        super( identifier, blockStates );
    }

    @Override
    public boolean placeBlock(@NotNull Player player, @NotNull World world, Vector blockPosition, @NotNull Vector placePosition, Vector clickedPosition, Item itemInHand, BlockFace blockFace ) {
        this.setDirection( player.getDirection() );
        this.setOpen( false );
        return super.placeBlock( player, world, blockPosition, placePosition, clickedPosition, itemInHand, blockFace );
    }

    @Override
    public boolean interact(@NotNull Player player, Vector blockPosition, Vector clickedPosition, BlockFace blockFace, Item itemInHand ) {
        Direction playerDirection = player.getDirection();
        Direction direction = this.getDirection();

        if ( playerDirection == Direction.NORTH ) {
            if ( direction == Direction.NORTH || direction == Direction.SOUTH ) {
                this.setDirection( Direction.NORTH );
            }
        } else if ( playerDirection == Direction.EAST ) {
            if ( direction == Direction.EAST || direction == Direction.WEST ) {
                this.setDirection( Direction.EAST );
            }
        } else if ( playerDirection == Direction.SOUTH ) {
            if ( direction == Direction.NORTH || direction == Direction.SOUTH ) {
                this.setDirection( Direction.SOUTH );
            }
        } else if ( playerDirection == Direction.WEST ) {
            if ( direction == Direction.EAST || direction == Direction.WEST ) {
                this.setDirection( Direction.WEST );
            }
        }
        this.setOpen( !this.isOpen() );
        this.location.getWorld().sendLevelEvent( this.location, LevelEventType.SOUND_DOOR_OPEN, 0 );
        return true;
    }

    public void setInWall( boolean value ) {
        this.setState( "in_wall_bit", value ? (byte) 1 : (byte) 0 );
    }

    public boolean isInWall() {
        return this.stateExists( "in_wall_bit" ) && this.getByteState( "in_wall_bit" ) == 1;
    }

    public void setOpen( boolean value ) {
        this.setState( "open_bit", value ? (byte) 1 : (byte) 0 );
    }

    public boolean isOpen() {
        return this.stateExists( "open_bit" ) && this.getByteState( "open_bit" ) == 1;
    }

    public void setDirection(@NotNull Direction direction ) {
        switch ( direction ) {
            case SOUTH -> this.setState( "direction", 0 );
            case WEST -> this.setState( "direction", 1 );
            case NORTH -> this.setState( "direction", 2 );
            case EAST -> this.setState( "direction", 3 );
        }
    }

    public @NotNull Direction getDirection() {
        int value = this.stateExists( "direction" ) ? this.getIntState( "direction" ) : 0;
        return switch ( value ) {
            case 0 -> Direction.SOUTH;
            case 1 -> Direction.WEST;
            case 2 -> Direction.NORTH;
            default -> Direction.EAST;
        };
    }
}
