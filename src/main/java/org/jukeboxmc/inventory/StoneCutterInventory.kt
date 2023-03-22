package org.jukeboxmc.inventory

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class StoneCutterInventory(holder: InventoryHolder?) : ContainerInventory(holder, -1, 2) {
    override val inventoryHolder: InventoryHolder
        get() = holder as Player
    override val type: InventoryType
        get() = InventoryType.STONE_CUTTER
    override val windowTypeId: ContainerType
        get() = ContainerType.STONECUTTER
}
