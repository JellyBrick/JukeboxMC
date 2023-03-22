package org.jukeboxmc.inventory

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType
import org.jukeboxmc.blockentity.BlockEntityBlastFurnace

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlastFurnaceInventory(holder: InventoryHolder?) : ContainerInventory(holder, -1, 3) {
    override val inventoryHolder: InventoryHolder
        get() = holder as BlockEntityBlastFurnace
    override val type: InventoryType
        get() = InventoryType.BLAST_FURNACE
    override val windowTypeId: ContainerType
        get() = ContainerType.BLAST_FURNACE
}
