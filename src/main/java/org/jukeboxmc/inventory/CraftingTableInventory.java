package org.jukeboxmc.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import org.jetbrains.annotations.NotNull;
import org.jukeboxmc.item.Item;
import org.jukeboxmc.player.Player;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class CraftingTableInventory extends ContainerInventory {

    public CraftingTableInventory( InventoryHolder holder ) {
        super( holder, -1, 9 );
    }

    @Override
    public Player getInventoryHolder() {
        return (Player) this.holder;
    }

    @Override
    public @NotNull InventoryType getType() {
        return InventoryType.WORKBENCH;
    }

    @Override
    public @NotNull ContainerType getWindowTypeId() {
        return ContainerType.WORKBENCH;
    }

    @Override
    public void onOpen(@NotNull Player player ) {
        super.onOpen( player );
        player.setCraftingGridInventory( new BigCraftingGridInventory( player ) );
    }

    @Override
    public void onClose(@NotNull Player player ) {
        player.setCraftingGridInventory( new SmallCraftingGridInventory( player ) );
    }

    @Override
    public void setItem(int slot, @NotNull Item item, boolean sendContent ) {
        super.setItem( slot - 32, item, sendContent );
    }

    @Override
    public Item getItem( int slot ) {
        return super.getItem( slot - 32 );
    }
}
