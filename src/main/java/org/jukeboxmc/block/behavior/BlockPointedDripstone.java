package org.jukeboxmc.block.behavior;

import com.nukkitx.nbt.NbtMap;
import org.jetbrains.annotations.NotNull;
import org.jukeboxmc.block.Block;
import org.jukeboxmc.block.data.DripstoneThickness;
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
public class BlockPointedDripstone extends Block {

    public BlockPointedDripstone( Identifier identifier ) {
        super( identifier );
    }

    public BlockPointedDripstone( Identifier identifier, NbtMap blockStates ) {
        super( identifier, blockStates );
    }

    @Override
    public boolean placeBlock(@NotNull Player player, @NotNull World world, Vector blockPosition, @NotNull Vector placePosition, Vector clickedPosition, Item itemInHand, BlockFace blockFace ) {
        Block upBlock = world.getBlock( placePosition ).getSide( BlockFace.UP );
        Block downBlock = world.getBlock( placePosition ).getSide( BlockFace.DOWN );
        if ( upBlock.isSolid() ) {
            this.setHanging( true );
            world.setBlock( placePosition, this );
            return true;
        } else if ( downBlock.isSolid() ) {
            this.setHanging( false );
            world.setBlock( placePosition, this );
        }
        return false;
    }

    public void setDripstoneThickness(@NotNull DripstoneThickness dripstoneThickness ) {
        this.setState( "dripstone_thickness", dripstoneThickness.name().toLowerCase() );
    }

    public @NotNull DripstoneThickness getDripstoneThickness() {
        return this.stateExists( "dripstone_thickness" ) ? DripstoneThickness.valueOf( this.getStringState( "dripstone_thickness" ) ) : DripstoneThickness.TIP;
    }

    public void setHanging( boolean value ) {
        this.setState( "hanging", value ? (byte) 1 : (byte) 0 );
    }

    public boolean isHanging() {
        return this.stateExists( "hanging" ) && this.getByteState( "hanging" ) == 1;
    }

}
