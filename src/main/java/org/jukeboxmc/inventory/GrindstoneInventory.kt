package org.jukeboxmc.inventory

import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerType
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class GrindstoneInventory(holder: InventoryHolder?) : ContainerInventory(holder, -1, 3) {
    override val inventoryHolder: InventoryHolder
        get() = holder as Player
    override val type: InventoryType
        get() = InventoryType.GRINDSTONE
    override val windowTypeId: ContainerType
        get() = ContainerType.GRINDSTONE
}
