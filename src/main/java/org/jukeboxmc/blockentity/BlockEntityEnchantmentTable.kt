package org.jukeboxmc.blockentity

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.item.Item
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockEntityEnchantmentTable(block: Block, blockEntityType: BlockEntityType) : BlockEntity(block, blockEntityType),
    InventoryHolder {
    private val enchantmentTableInventory: EnchantmentTableInventory

    init {
        enchantmentTableInventory = EnchantmentTableInventory(this)
    }

    override fun interact(
        player: Player,
        blockPosition: Vector,
        clickedPosition: Vector?,
        blockFace: BlockFace?,
        itemInHand: Item?
    ): Boolean {
        player.openInventory(enchantmentTableInventory, blockPosition)
        return true
    }

    fun getEnchantmentTableInventory(): EnchantmentTableInventory {
        return enchantmentTableInventory
    }
}