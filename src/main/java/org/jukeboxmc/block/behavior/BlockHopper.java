package org.jukeboxmc.block.behavior;

import com.nukkitx.nbt.NbtMap;
import org.jetbrains.annotations.NotNull;
import org.jukeboxmc.block.Block;
import org.jukeboxmc.block.direction.BlockFace;
import org.jukeboxmc.blockentity.BlockEntity;
import org.jukeboxmc.blockentity.BlockEntityHopper;
import org.jukeboxmc.blockentity.BlockEntityType;
import org.jukeboxmc.item.Item;
import org.jukeboxmc.math.Vector;
import org.jukeboxmc.player.Player;
import org.jukeboxmc.util.Identifier;
import org.jukeboxmc.world.World;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class BlockHopper extends Block {

    public BlockHopper( Identifier identifier ) {
        super( identifier );
    }

    public BlockHopper( Identifier identifier, NbtMap blockStates ) {
        super( identifier, blockStates );
    }

    @Override
    public boolean placeBlock(@NotNull Player player, @NotNull World world, Vector blockPosition, @NotNull Vector placePosition, Vector clickedPosition, Item itemInHand, @NotNull BlockFace blockFace ) {
        this.setBlockFace( blockFace == BlockFace.UP ? BlockFace.DOWN : blockFace.opposite() );
        this.setToggle( false );
        boolean value = super.placeBlock( player, world, blockPosition, placePosition, clickedPosition, itemInHand, blockFace );
        if ( value ) {
            BlockEntity.create( BlockEntityType.HOPPER, this ).spawn();
        }
        return value;
    }

    @Override
    public boolean interact(@NotNull Player player, Vector blockPosition, Vector clickedPosition, BlockFace blockFace, Item itemInHand ) {
        BlockEntityHopper blockEntity = this.getBlockEntity();
        if ( blockEntity != null ) {
            blockEntity.interact( player, blockPosition, clickedPosition, blockFace, itemInHand );
            return true;
        }
        return false;
    }

    @Override
    public BlockEntityHopper getBlockEntity() {
        return (BlockEntityHopper) this.location.getWorld().getBlockEntity( this.location, this.location.getDimension() );
    }

    public void setToggle( boolean value ) {
        this.setState( "toggle_bit", value ? (byte) 1 : (byte) 0 );
    }

    public boolean isToggle() {
        return this.stateExists( "toggle_bit" ) && this.getByteState( "toggle_bit" ) == 1;
    }

    public void setBlockFace(@NotNull BlockFace blockFace ) {
        this.setState( "facing_direction", blockFace.ordinal() );
    }

    public BlockFace getBlockFace() {
        return this.stateExists( "facing_direction" ) ? BlockFace.values()[this.getIntState( "facing_direction" )] : BlockFace.NORTH;
    }
}
