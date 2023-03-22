package org.jukeboxmc.blockentity

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.inventory.HopperInventory
import org.jukeboxmc.inventory.InventoryHolder
import org.jukeboxmc.item.Item
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockEntityHopper(
    block: Block,
    blockEntityType: BlockEntityType,
) : BlockEntity(block, blockEntityType), InventoryHolder {
    private val hopperInventory: HopperInventory

    init {
        hopperInventory = HopperInventory(this)
    }

    override fun interact(
        player: Player,
        blockPosition: Vector,
        clickedPosition: Vector?,
        blockFace: BlockFace?,
        itemInHand: Item?,
    ): Boolean {
        player.openInventory(hopperInventory, blockPosition)
        return true
    }

    fun getHopperInventory(): HopperInventory {
        return hopperInventory
    }
}
