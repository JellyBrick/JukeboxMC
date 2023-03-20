package org.jukeboxmc.block.behavior;

import com.nukkitx.nbt.NbtMap;
import org.jetbrains.annotations.NotNull;
import org.jukeboxmc.block.data.StoneSlab3Type;
import org.jukeboxmc.util.Identifier;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class BlockDoubleStoneSlab3 extends BlockSlab {

    public BlockDoubleStoneSlab3( Identifier identifier ) {
        super( identifier );
    }

    public BlockDoubleStoneSlab3( Identifier identifier, NbtMap blockStates ) {
        super( identifier, blockStates );
    }

    public BlockDoubleStoneSlab3 setStoneSlabType(@NotNull StoneSlab3Type stoneSlabType ) {
        return this.setState( "stone_slab_type_3", stoneSlabType.name().toLowerCase() );
    }

    public @NotNull StoneSlab3Type getStoneSlabType() {
        return this.stateExists( "stone_slab_type_3" ) ? StoneSlab3Type.valueOf( this.getStringState( "stone_slab_type_3" ) ) : StoneSlab3Type.END_STONE_BRICK;
    }
}
