package org.jukeboxmc.blockentity

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.inventory.DropperInventory
import org.jukeboxmc.inventory.InventoryHolder
import org.jukeboxmc.item.Item
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockEntityDropper(
    block: Block,
    blockEntityType: BlockEntityType,
) : BlockEntity(block, blockEntityType), InventoryHolder {
    private val dropperInventory: DropperInventory = DropperInventory(this)

    override fun interact(
        player: Player,
        blockPosition: Vector,
        clickedPosition: Vector?,
        blockFace: BlockFace?,
        itemInHand: Item?,
    ): Boolean {
        player.openInventory(dropperInventory, blockPosition)
        return true
    }

    fun getDropperInventory(): DropperInventory {
        return dropperInventory
    }
}
