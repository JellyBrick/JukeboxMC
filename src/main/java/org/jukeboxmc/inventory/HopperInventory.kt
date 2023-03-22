package org.jukeboxmc.inventory

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType
import org.jukeboxmc.blockentity.BlockEntityHopper

/**
 * @author LucGamesYT
 * @version 1.0
 */
class HopperInventory(holder: InventoryHolder?) : ContainerInventory(holder, -1, 5) {
    override val inventoryHolder: InventoryHolder
        get() = holder as BlockEntityHopper
    override val type: InventoryType
        get() = InventoryType.HOPPER
    override val windowTypeId: ContainerType
        get() = ContainerType.HOPPER
}
