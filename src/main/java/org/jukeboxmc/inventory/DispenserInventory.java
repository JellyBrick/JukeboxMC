package org.jukeboxmc.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import org.jetbrains.annotations.NotNull;
import org.jukeboxmc.blockentity.BlockEntityDispenser;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class DispenserInventory extends ContainerInventory {

    public DispenserInventory( InventoryHolder holder ) {
        super( holder, -1, 9 );
    }

    @Override
    public BlockEntityDispenser getInventoryHolder() {
        return (BlockEntityDispenser) this.holder;
    }

    @Override
    public @NotNull InventoryType getType() {
        return InventoryType.DISPENSER;
    }

    @Override
    public @NotNull ContainerType getWindowTypeId() {
        return ContainerType.DISPENSER;
    }
}
