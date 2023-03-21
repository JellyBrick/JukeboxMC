package org.jukeboxmc.inventory

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType
import org.jukeboxmc.blockentity.BlockEntityBrewingStand

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BrewingStandInventory(holder: InventoryHolder?) : ContainerInventory(holder, -1, 5) {
    override val inventoryHolder: InventoryHolder?
        get() = holder as BlockEntityBrewingStand
    override val type: InventoryType
        get() = InventoryType.BREWING_STAND
    override val windowTypeId: ContainerType
        get() = ContainerType.BREWING_STAND
}