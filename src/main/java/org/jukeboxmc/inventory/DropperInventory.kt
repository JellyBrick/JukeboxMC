package org.jukeboxmc.inventory

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType
import org.jukeboxmc.blockentity.BlockEntityDropper

/**
 * @author LucGamesYT
 * @version 1.0
 */
class DropperInventory(holder: InventoryHolder?) : ContainerInventory(holder, -1, 9) {
    override val inventoryHolder: InventoryHolder
        get() = holder as BlockEntityDropper
    override val type: InventoryType
        get() = InventoryType.DROPPER
    override val windowTypeId: ContainerType
        get() = ContainerType.DROPPER
}
