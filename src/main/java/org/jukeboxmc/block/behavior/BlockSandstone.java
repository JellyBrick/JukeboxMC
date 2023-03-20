package org.jukeboxmc.block.behavior;

import com.nukkitx.nbt.NbtMap;
import org.jetbrains.annotations.NotNull;
import org.jukeboxmc.block.Block;
import org.jukeboxmc.block.data.SandStoneType;
import org.jukeboxmc.item.Item;
import org.jukeboxmc.item.ItemType;
import org.jukeboxmc.item.behavior.ItemSandstone;
import org.jukeboxmc.util.Identifier;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class BlockSandstone extends Block {

    public BlockSandstone( Identifier identifier ) {
        super( identifier );
    }

    public BlockSandstone( Identifier identifier, NbtMap blockStates ) {
        super( identifier, blockStates );
    }

    @Override
    public Item toItem() {
        return Item.<ItemSandstone>create( ItemType.SANDSTONE ).setSandStoneType( this.getSandStoneType() );
    }

    public @NotNull BlockSandstone setSandStoneType(@NotNull SandStoneType sandStoneType ) {
        this.setState( "sand_stone_type", sandStoneType.name().toLowerCase() );
        return this;
    }

    public @NotNull SandStoneType getSandStoneType() {
        return this.stateExists( "sand_stone_type" ) ? SandStoneType.valueOf( this.getStringState( "sand_stone_type" ) ) : SandStoneType.DEFAULT;
    }
}
