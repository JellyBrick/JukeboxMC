package org.jukeboxmc.inventory

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class SmithingTableInventory(holder: InventoryHolder?) : ContainerInventory(holder, -1, 3) {
    override val inventoryHolder: InventoryHolder?
        get() = holder as Player
    override val type: InventoryType
        get() = InventoryType.SMITHING_TABLE
    override val windowTypeId: ContainerType
        get() = ContainerType.SMITHING_TABLE
}