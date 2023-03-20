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
public class BlockLimeWool extends Block {

    public BlockLimeWool(Identifier identifier ) {
        super( identifier );
    }

    public BlockLimeWool(Identifier identifier, NbtMap blockStates ) {
        super( identifier, blockStates );
    }

    @Override
    public @NotNull Item toItem() {
        return Item.create( ItemType.LIME_WOOL );
    }
}
