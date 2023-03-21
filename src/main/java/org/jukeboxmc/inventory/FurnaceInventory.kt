package org.jukeboxmc.inventory

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType
import org.jukeboxmc.blockentity.BlockEntityFurnace

/**
 * @author LucGamesYT
 * @version 1.0
 */
class FurnaceInventory(holder: InventoryHolder?) : ContainerInventory(holder, -1, 3) {
    override val inventoryHolder: InventoryHolder?
        get() = holder as BlockEntityFurnace
    override val type: InventoryType
        get() = InventoryType.FURNACE
    override val windowTypeId: ContainerType
        get() = ContainerType.FURNACE
}