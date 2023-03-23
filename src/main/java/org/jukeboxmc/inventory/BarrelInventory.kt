package org.jukeboxmc.inventory

import org.cloudburstmc.protocol.bedrock.data.SoundEvent
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerType
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.behavior.BlockBarrel
import org.jukeboxmc.blockentity.BlockEntityBarrel
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BarrelInventory(holder: InventoryHolder?) : ContainerInventory(holder, -1, 27) {
    override val inventoryHolder: BlockEntityBarrel
        get() = holder as BlockEntityBarrel
    override val type: InventoryType
        get() = InventoryType.BARREL
    override val windowTypeId: ContainerType
        get() = ContainerType.CONTAINER

    override fun onOpen(player: Player) {
        super.onOpen(player)
        if (viewer.size == 1) {
            val block: Block = inventoryHolder.block
            if (block is BlockBarrel) {
                if (!block.isOpen) {
                    block.isOpen = true
                    player.world?.playSound(block.location, SoundEvent.BARREL_OPEN)
                }
            }
        }
    }

    override fun onClose(player: Player) {
        if (viewer.size == 0) {
            val block: Block = inventoryHolder.block
            if (block is BlockBarrel) {
                if (block.isOpen) {
                    block.isOpen = false
                    player.world?.playSound(block.location, SoundEvent.BARREL_CLOSE)
                }
            }
        }
    }
}
