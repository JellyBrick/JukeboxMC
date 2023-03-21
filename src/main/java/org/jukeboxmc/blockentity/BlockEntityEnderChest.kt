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
class BlockEntityEnderChest(block: Block, blockEntityType: BlockEntityType) : BlockEntity(block, blockEntityType) {
    override fun interact(
        player: Player,
        blockPosition: Vector,
        clickedPosition: Vector?,
        blockFace: BlockFace?,
        itemInHand: Item?
    ): Boolean {
        player.enderChestInventory.position = blockPosition
        player.openInventory(player.enderChestInventory, blockPosition)
        return true
    }
}