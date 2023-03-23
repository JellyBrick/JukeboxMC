package org.jukeboxmc.inventory

import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerType
import org.jukeboxmc.blockentity.BlockEntityLoom

/**
 * @author LucGamesYT
 * @version 1.0
 */
class LoomInventory(holder: InventoryHolder?) : ContainerInventory(holder, -1, 4) {
    override val inventoryHolder: InventoryHolder
        get() = holder as BlockEntityLoom
    override val type: InventoryType
        get() = InventoryType.LOOM
    override val windowTypeId: ContainerType
        get() = ContainerType.LOOM
}
