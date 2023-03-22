package org.jukeboxmc.blockentity

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.inventory.InventoryHolder
import org.jukeboxmc.inventory.SmokerInventory
import org.jukeboxmc.item.Item
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockEntitySmoker(
    block: Block,
    blockEntityType: BlockEntityType,
) : BlockEntity(block, blockEntityType), InventoryHolder {
    private val smokerInventory: SmokerInventory

    init {
        smokerInventory = SmokerInventory(this)
    }

    override fun interact(
        player: Player,
        blockPosition: Vector,
        clickedPosition: Vector?,
        blockFace: BlockFace?,
        itemInHand: Item?,
    ): Boolean {
        player.openInventory(smokerInventory, blockPosition)
        return true
    }

    fun getSmokerInventory(): SmokerInventory {
        return smokerInventory
    }
}
