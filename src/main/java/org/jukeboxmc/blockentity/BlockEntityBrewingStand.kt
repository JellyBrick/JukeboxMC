package org.jukeboxmc.blockentity

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.inventory.BrewingStandInventory
import org.jukeboxmc.item.Item
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockEntityBrewingStand(block: Block, blockEntityType: BlockEntityType) : BlockEntity(block, blockEntityType),
    InventoryHolder {
    val brewingStandInventory: BrewingStandInventory

    init {
        brewingStandInventory = BrewingStandInventory(this)
    }

    override fun interact(
        player: Player,
        blockPosition: Vector,
        clickedPosition: Vector?,
        blockFace: BlockFace?,
        itemInHand: Item?
    ): Boolean {
        player.openInventory(brewingStandInventory, blockPosition)
        return true
    }
}