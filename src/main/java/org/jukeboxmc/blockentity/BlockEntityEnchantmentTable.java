package org.jukeboxmc.blockentity;

import org.jetbrains.annotations.NotNull;
import org.jukeboxmc.block.Block;
import org.jukeboxmc.block.direction.BlockFace;
import org.jukeboxmc.inventory.EnchantmentTableInventory;
import org.jukeboxmc.inventory.InventoryHolder;
import org.jukeboxmc.item.Item;
import org.jukeboxmc.math.Vector;
import org.jukeboxmc.player.Player;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class BlockEntityEnchantmentTable extends BlockEntity implements InventoryHolder {

    private final @NotNull EnchantmentTableInventory enchantmentTableInventory;

    public BlockEntityEnchantmentTable(@NotNull Block block, BlockEntityType blockEntityType ) {
        super( block, blockEntityType );
        this.enchantmentTableInventory = new EnchantmentTableInventory( this );
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull Vector blockPosition, Vector clickedPosition, BlockFace blockFace, Item itemInHand ) {
        player.openInventory( this.enchantmentTableInventory, blockPosition );
        return true;
    }

    public @NotNull EnchantmentTableInventory getEnchantmentTableInventory() {
        return this.enchantmentTableInventory;
    }
}
