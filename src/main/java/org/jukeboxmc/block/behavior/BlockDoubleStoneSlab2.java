package org.jukeboxmc.block.behavior;

import com.nukkitx.nbt.NbtMap;
import org.jetbrains.annotations.NotNull;
import org.jukeboxmc.block.data.StoneSlab2Type;
import org.jukeboxmc.util.Identifier;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class BlockDoubleStoneSlab2 extends BlockSlab {

    public BlockDoubleStoneSlab2( Identifier identifier ) {
        super( identifier );
    }

    public BlockDoubleStoneSlab2( Identifier identifier, NbtMap blockStates ) {
        super( identifier, blockStates );
    }

    public BlockDoubleStoneSlab2 setStoneSlabType(@NotNull StoneSlab2Type stoneSlabType ) {
        return this.setState( "stone_slab_type_2", stoneSlabType.name().toLowerCase() );
    }

    public @NotNull StoneSlab2Type getStoneSlabType() {
        return this.stateExists( "stone_slab_type_2" ) ? StoneSlab2Type.valueOf( this.getStringState( "stone_slab_type_2" ) ) : StoneSlab2Type.RED_SANDSTONE;
    }
}
