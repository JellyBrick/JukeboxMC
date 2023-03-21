package org.jukeboxmc.inventory

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType
import org.jukeboxmc.blockentity.BlockEntityDispenser

/**
 * @author LucGamesYT
 * @version 1.0
 */
class DispenserInventory(holder: InventoryHolder?) : ContainerInventory(holder, -1, 9) {
    override val inventoryHolder: InventoryHolder?
        get() = holder as BlockEntityDispenser
    override val type: InventoryType
        get() = InventoryType.DISPENSER
    override val windowTypeId: ContainerType
        get() = ContainerType.DISPENSER
}