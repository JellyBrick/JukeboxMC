package org.jukeboxmc.block.behavior;

import com.nukkitx.nbt.NbtMap;
import org.jetbrains.annotations.NotNull;
import org.jukeboxmc.block.Block;
import org.jukeboxmc.block.BlockType;
import org.jukeboxmc.block.data.StoneSlab4Type;
import org.jukeboxmc.block.direction.BlockFace;
import org.jukeboxmc.item.Item;
import org.jukeboxmc.math.Vector;
import org.jukeboxmc.player.Player;
import org.jukeboxmc.util.Identifier;
import org.jukeboxmc.world.World;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class BlockStoneSlab4 extends BlockSlab {

    public BlockStoneSlab4( Identifier identifier ) {
        super( identifier );
    }

    public BlockStoneSlab4( Identifier identifier, NbtMap blockStates ) {
        super( identifier, blockStates );
    }

    @Override
    public boolean placeBlock(@NotNull Player player, @NotNull World world, Vector blockPosition, @NotNull Vector placePosition, @NotNull Vector clickedPosition, Item itemInHand, BlockFace blockFace ) {
        Block targetBlock = world.getBlock( blockPosition );
        Block block = world.getBlock( placePosition );

        if ( blockFace == BlockFace.DOWN ) {
            if ( targetBlock instanceof BlockStoneSlab4 && ( (BlockStoneSlab4) targetBlock ).isTopSlot() && ( (BlockStoneSlab4) targetBlock ).getStoneSlabType() == this.getStoneSlabType() ) {
                world.setBlock( blockPosition, Block.<BlockDoubleStoneSlab4>create( BlockType.DOUBLE_STONE_BLOCK_SLAB4 ).setStoneSlabType( this.getStoneSlabType() ) );
                return true;
            } else if ( block instanceof BlockStoneSlab4 && ( (BlockStoneSlab4) block ).getStoneSlabType() == this.getStoneSlabType() ) {
                world.setBlock( placePosition, Block.<BlockDoubleStoneSlab4>create( BlockType.DOUBLE_STONE_BLOCK_SLAB4 ).setStoneSlabType( this.getStoneSlabType() ) );
                return true;
            }
        } else if ( blockFace == BlockFace.UP ) {
            if ( targetBlock instanceof BlockStoneSlab4 && !( (BlockStoneSlab4) targetBlock ).isTopSlot() && ( (BlockStoneSlab4) targetBlock ).getStoneSlabType() == this.getStoneSlabType() ) {
                world.setBlock( blockPosition, Block.<BlockDoubleStoneSlab4>create( BlockType.DOUBLE_STONE_BLOCK_SLAB4 ).setStoneSlabType( this.getStoneSlabType() ) );
                return true;
            } else if ( block instanceof BlockStoneSlab4 && ( (BlockStoneSlab4) block ).getStoneSlabType() == this.getStoneSlabType() ) {
                world.setBlock( placePosition, Block.<BlockDoubleStoneSlab4>create( BlockType.DOUBLE_STONE_BLOCK_SLAB4 ).setStoneSlabType( this.getStoneSlabType() ) );
                return true;
            }
        } else {
            if ( block instanceof BlockStoneSlab4 && ( (BlockStoneSlab4) block ).getStoneSlabType() == this.getStoneSlabType() ) {
                world.setBlock( placePosition, Block.<BlockDoubleStoneSlab4>create( BlockType.DOUBLE_STONE_BLOCK_SLAB4 ).setStoneSlabType( this.getStoneSlabType() ) );
                return true;
            } else {
                this.setTopSlot( clickedPosition.getY() > 0.5 && !world.getBlock( blockPosition ).canBeReplaced( this ) );
            }
        }
        super.placeBlock( player, world, blockPosition, placePosition, clickedPosition, itemInHand, blockFace );
        world.setBlock( placePosition, this );
        return true;
    }

    public BlockStoneSlab4 setStoneSlabType(@NotNull StoneSlab4Type stoneSlabType ) {
        return this.setState( "stone_slab_type_4", stoneSlabType.name().toLowerCase() );
    }

    public @NotNull StoneSlab4Type getStoneSlabType() {
        return this.stateExists( "stone_slab_type_4" ) ? StoneSlab4Type.valueOf( this.getStringState( "stone_slab_type_4" ) ) : StoneSlab4Type.STONE;
    }
}
