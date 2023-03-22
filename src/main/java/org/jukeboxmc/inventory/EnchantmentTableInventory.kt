package org.jukeboxmc.inventory

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType
import org.jukeboxmc.blockentity.BlockEntityEnchantmentTable

/**
 * @author LucGamesYT
 * @version 1.0
 */
class EnchantmentTableInventory(holder: InventoryHolder?) : ContainerInventory(holder, -1, 2) {
    override val inventoryHolder: InventoryHolder
        get() = holder as BlockEntityEnchantmentTable
    override val type: InventoryType
        get() = InventoryType.ENCHANTMENT_TABLE
    override val windowTypeId: ContainerType
        get() = ContainerType.ENCHANTMENT
}
