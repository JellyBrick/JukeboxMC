package org.jukeboxmc.blockentity;

import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtMapBuilder;
import com.nukkitx.nbt.NbtType;
import org.jetbrains.annotations.NotNull;
import org.jukeboxmc.block.Block;
import org.jukeboxmc.block.direction.BlockFace;
import org.jukeboxmc.inventory.BlastFurnaceInventory;
import org.jukeboxmc.inventory.InventoryHolder;
import org.jukeboxmc.item.Item;
import org.jukeboxmc.math.Vector;
import org.jukeboxmc.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class BlockEntityBlastFurnace extends SmeltingComponent implements InventoryHolder {

    private final @NotNull BlastFurnaceInventory blastFurnaceInventory;

    public BlockEntityBlastFurnace(@NotNull Block block, BlockEntityType blockEntityType ) {
        super( block, blockEntityType );
        this.blastFurnaceInventory = new BlastFurnaceInventory( this );
        this.initInventory( this.blastFurnaceInventory );
    }

    @Override
    public boolean interact(@NotNull Player player, @NotNull Vector blockPosition, Vector clickedPosition, BlockFace blockFace, Item itemInHand ) {
        player.openInventory( this.blastFurnaceInventory, blockPosition );
        return super.interact( player, blockPosition, clickedPosition, blockFace, itemInHand );
    }

    @Override
    public void fromCompound(@NotNull NbtMap compound ) {
        super.fromCompound( compound );

        List<NbtMap> items = compound.getList( "Items", NbtType.COMPOUND );
        for ( NbtMap nbtMap : items ) {
            Item item = this.toItem( nbtMap );
            byte slot = nbtMap.getByte( "Slot", (byte) 127 );
            if ( slot == 127 ) {
                this.blastFurnaceInventory.addItem( item, false );
            } else {
                this.blastFurnaceInventory.setItem( slot, item, false );
            }
        }
    }

    @Override
    public @NotNull NbtMapBuilder toCompound() {
        NbtMapBuilder builder = super.toCompound();

        List<NbtMap> itemsCompoundList = new ArrayList<>();
        for ( int slot = 0; slot < this.blastFurnaceInventory.getSize(); slot++ ) {
            NbtMapBuilder itemCompound = NbtMap.builder();
            Item item = this.blastFurnaceInventory.getItem( slot );

            itemCompound.putByte( "Slot", (byte) slot );
            this.fromItem( item, itemCompound );

            itemsCompoundList.add( itemCompound.build() );
        }

        builder.putList( "Items", NbtType.COMPOUND, itemsCompoundList );
        return builder;
    }

    public @NotNull BlastFurnaceInventory getBlastFurnaceInventory() {
        return this.blastFurnaceInventory;
    }
}
