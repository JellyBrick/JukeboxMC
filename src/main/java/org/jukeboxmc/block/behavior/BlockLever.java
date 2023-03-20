package org.jukeboxmc.block.behavior;

import com.nukkitx.nbt.NbtMap;
import org.jetbrains.annotations.NotNull;
import org.jukeboxmc.block.Block;
import org.jukeboxmc.block.data.LeverDirection;
import org.jukeboxmc.block.direction.BlockFace;
import org.jukeboxmc.item.Item;
import org.jukeboxmc.math.Vector;
import org.jukeboxmc.player.Player;
import org.jukeboxmc.util.Identifier;
import org.jukeboxmc.world.Sound;
import org.jukeboxmc.world.World;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class BlockLever extends Block {

    public BlockLever( Identifier identifier ) {
        super( identifier );
    }

    public BlockLever( Identifier identifier, NbtMap blockStates ) {
        super( identifier, blockStates );
    }

    @Override
    public boolean placeBlock(@NotNull Player player, @NotNull World world, Vector blockPosition, @NotNull Vector placePosition, Vector clickedPosition, Item itemInHand, @NotNull BlockFace blockFace ) {
        Block block = world.getBlock( blockPosition );

        if ( block.isTransparent() ) {
            return false;
        }

        this.setLeverDirection( LeverDirection.forDirection( blockFace, player.getDirection().opposite() ) );

        world.setBlock( placePosition, this, 0 );
        return true;
    }

    @Override
    public boolean interact(@NotNull Player player, Vector blockPosition, Vector clickedPosition, BlockFace blockFace, Item itemInHand ) {
        boolean open = this.isOpen();
        this.setOpen( !open );
        player.playSound( this.location, Sound.RANDOM_CLICK, 0.8f, open ? 0.5f : 0.58f, true );
        return true;
    }

    public void setOpen( boolean value ) {
        this.setState( "open_bit", value ? (byte) 1 : (byte) 0 );
    }

    public boolean isOpen() {
        return this.stateExists( "open_bit" ) && this.getByteState( "open_bit" ) == 1;
    }

    public void setLeverDirection(@NotNull LeverDirection leverDirection ) {
        this.setState( "lever_direction", leverDirection.name().toLowerCase() );
    }

    public @NotNull LeverDirection getLeverDirection() {
        return this.stateExists( "lever_direction" ) ? LeverDirection.valueOf( this.getStringState( "lever_direction" ) ) : LeverDirection.DOWN_EAST_WEST;
    }
}
