package org.jukeboxmc.inventory

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType
import org.jukeboxmc.blockentity.BlockEntitySmoker

/**
 * @author LucGamesYT
 * @version 1.0
 */
class SmokerInventory(holder: InventoryHolder?) : ContainerInventory(holder, -1, 3) {
    override val inventoryHolder: InventoryHolder
        get() = holder as BlockEntitySmoker
    override val type: InventoryType
        get() = InventoryType.SMOKER
    override val windowTypeId: ContainerType
        get() = ContainerType.SMOKER
}
