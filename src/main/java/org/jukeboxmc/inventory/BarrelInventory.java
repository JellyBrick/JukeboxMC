package org.jukeboxmc.inventory;

import com.nukkitx.protocol.bedrock.data.SoundEvent;
import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import org.jetbrains.annotations.NotNull;
import org.jukeboxmc.block.Block;
import org.jukeboxmc.block.behavior.BlockBarrel;
import org.jukeboxmc.blockentity.BlockEntityBarrel;
import org.jukeboxmc.player.Player;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class BarrelInventory extends ContainerInventory {

    public BarrelInventory( InventoryHolder holder ) {
        super( holder, -1, 27 );
    }

    @Override
    public BlockEntityBarrel getInventoryHolder() {
        return (BlockEntityBarrel) this.holder;
    }

    @Override
    public @NotNull InventoryType getType() {
        return InventoryType.BARREL;
    }

    @Override
    public @NotNull ContainerType getWindowTypeId() {
        return ContainerType.CONTAINER;
    }

    @Override
    public void onOpen(@NotNull Player player ) {
        super.onOpen( player );
        if ( this.viewer.size() == 1 ) {
            Block block = this.getInventoryHolder().getBlock();
            if ( block instanceof BlockBarrel blockBarrel ) {
                if ( !blockBarrel.isOpen() ) {
                    blockBarrel.setOpen( true );
                    player.getWorld().playSound( block.getLocation(), SoundEvent.BARREL_OPEN );
                }
            }
        }
    }

    @Override
    public void onClose(@NotNull Player player ) {
        if ( this.viewer.size() == 0 ) {
            Block block = this.getInventoryHolder().getBlock();
            if ( block instanceof BlockBarrel blockBarrel ) {
                if ( blockBarrel.isOpen() ) {
                    blockBarrel.setOpen( false );
                    player.getWorld().playSound( blockBarrel.getLocation(), SoundEvent.BARREL_CLOSE );
                }
            }
        }
    }
}
