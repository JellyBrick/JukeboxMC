package org.jukeboxmc.block.behavior;

import com.nukkitx.nbt.NbtMap;
import org.jetbrains.annotations.NotNull;
import org.jukeboxmc.block.Block;
import org.jukeboxmc.block.direction.BlockFace;
import org.jukeboxmc.block.direction.Direction;
import org.jukeboxmc.blockentity.BlockEntity;
import org.jukeboxmc.blockentity.BlockEntityChest;
import org.jukeboxmc.blockentity.BlockEntityType;
import org.jukeboxmc.inventory.ChestInventory;
import org.jukeboxmc.item.Item;
import org.jukeboxmc.item.ItemType;
import org.jukeboxmc.math.Vector;
import org.jukeboxmc.player.Player;
import org.jukeboxmc.util.Identifier;
import org.jukeboxmc.world.World;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class BlockChest extends Block {

    public BlockChest( Identifier identifier ) {
        super( identifier );
    }

    public BlockChest( Identifier identifier, NbtMap blockStates ) {
        super( identifier, blockStates );
    }

    @Override
    public boolean placeBlock(@NotNull Player player, @NotNull World world, Vector blockPosition, @NotNull Vector placePosition, Vector clickedPosition, Item itemInHand, BlockFace blockFace ) {
        this.setBlockFace( player.getDirection().toBlockFace().opposite() );
        boolean value = super.placeBlock( player, world, blockPosition, placePosition, clickedPosition, itemInHand, blockFace );
        if ( value ) {
            BlockEntity.create( BlockEntityType.CHEST, this ).spawn();
            for ( Direction direction : Direction.values() ) {
                Block side = this.getSide( direction );
                if ( side.getType().equals( this.getType() )) {
                    BlockEntityChest blockEntity = this.getBlockEntity();
                    BlockEntityChest sideBlockEntity = (BlockEntityChest) side.getBlockEntity();
                    if ( blockEntity != null && sideBlockEntity != null && !blockEntity.isPaired() && !sideBlockEntity.isPaired()) {
                        blockEntity.pair( sideBlockEntity );
                        blockEntity.update( player );
                        sideBlockEntity.update( player );
                    }

                }
            }
        }
        return value;
    }

    @Override
    public boolean interact(@NotNull Player player, Vector blockPosition, Vector clickedPosition, BlockFace blockFace, Item itemInHand ) {
        BlockEntityChest blockEntity = this.getBlockEntity();
        if ( blockEntity != null ) {
            blockEntity.interact( player, blockPosition, clickedPosition, blockFace, itemInHand );
            return true;
        }
        return false;
    }

    @Override
    public void onBlockBreak(@NotNull Vector breakPosition ) {
        BlockEntityChest blockEntity = this.getBlockEntity();
        if ( blockEntity != null ) {
            if ( blockEntity.isPaired() ) {
                blockEntity.unpair();
            }
            ChestInventory chestInventory = blockEntity.getChestInventory();
            for ( Item content : chestInventory.getContents() ) {
                if ( content != null && !content.getType().equals( ItemType.AIR ) ){
                    this.location.getWorld().dropItem( content, breakPosition, null ).spawn();
                }
            }
            chestInventory.clear();
            chestInventory.getViewer().clear();
        }
        super.onBlockBreak( breakPosition );
    }

    @Override
    public BlockEntityChest getBlockEntity() {
        return (BlockEntityChest) this.location.getWorld().getBlockEntity( this.location, this.location.getDimension() );
    }

    public void setBlockFace(@NotNull BlockFace blockFace ) {
        this.setState( "facing_direction", blockFace.ordinal() );
    }

    public BlockFace getBlockFace() {
        return this.stateExists( "facing_direction" ) ? BlockFace.values()[this.getIntState( "facing_direction" )] : BlockFace.NORTH;
    }
}
