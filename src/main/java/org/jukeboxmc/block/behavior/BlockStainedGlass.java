package org.jukeboxmc.block.behavior;

import com.nukkitx.nbt.NbtMap;
import org.jetbrains.annotations.NotNull;
import org.jukeboxmc.block.Block;
import org.jukeboxmc.block.data.BlockColor;
import org.jukeboxmc.item.Item;
import org.jukeboxmc.item.ItemType;
import org.jukeboxmc.item.behavior.ItemStainedGlass;
import org.jukeboxmc.util.Identifier;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class BlockStainedGlass extends Block {

    public BlockStainedGlass( Identifier identifier ) {
        super( identifier );
    }

    public BlockStainedGlass( Identifier identifier, NbtMap blockStates ) {
        super( identifier, blockStates );
    }

    @Override
    public Item toItem() {
        return Item.<ItemStainedGlass>create( ItemType.STAINED_GLASS ).setColor( this.getColor() );
    }

    public @NotNull BlockStainedGlass setColor(@NotNull BlockColor color ) {
        this.setState( "color", color.name().toLowerCase() );
        return this;
    }

    public @NotNull BlockColor getColor() {
        return this.stateExists( "color" ) ? BlockColor.valueOf( this.getStringState( "color" ) ) : BlockColor.WHITE;
    }
}
