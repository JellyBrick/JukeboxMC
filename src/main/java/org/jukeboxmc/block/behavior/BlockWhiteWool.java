package org.jukeboxmc.block.behavior;

import com.nukkitx.nbt.NbtMap;
import org.jetbrains.annotations.NotNull;
import org.jukeboxmc.block.Block;
import org.jukeboxmc.item.Item;
import org.jukeboxmc.item.ItemType;
import org.jukeboxmc.util.Identifier;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class BlockWhiteWool extends Block {

    public BlockWhiteWool(Identifier identifier ) {
        super( identifier );
    }

    public BlockWhiteWool(Identifier identifier, NbtMap blockStates ) {
        super( identifier, blockStates );
    }

    @Override
    public @NotNull Item toItem() {
        return Item.create( ItemType.WHITE_WOOL );
    }
}
