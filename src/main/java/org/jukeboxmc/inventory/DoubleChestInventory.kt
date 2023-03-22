package org.jukeboxmc.inventory

import com.nukkitx.protocol.bedrock.data.SoundEvent
import com.nukkitx.protocol.bedrock.data.inventory.ContainerType
import com.nukkitx.protocol.bedrock.packet.BlockEventPacket
import org.jukeboxmc.blockentity.BlockEntityChest
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class DoubleChestInventory(
    holder: InventoryHolder?,
    private val left: ChestInventory,
    private val right: ChestInventory,
) : ChestInventory(holder, left.size + right.size), InventoryHolder {
    override fun clear() {
        right.clear()
        left.clear()
    }

    override val inventoryHolder: BlockEntityChest
        get() = holder as BlockEntityChest
    override val type: InventoryType
        get() = InventoryType.DOUBLE_CHEST
    override val windowTypeId: ContainerType
        get() = ContainerType.CONTAINER

    override fun setItem(slot: Int, item: Item, sendContent: Boolean) {
        if (slot < left.size) {
            left.setItem(slot, item)
            if (sendContent) {
                for (player in viewer) {
                    this.sendContents(slot, player)
                }
            }
            return
        }
        right.setItem(slot - left.size, item)
        if (sendContent) {
            for (player in viewer) {
                this.sendContents(slot, player)
            }
        }
    }

    override fun getItem(slot: Int): Item {
        return if (slot < left.size) left.getItem(slot) else right.getItem(slot - left.size)
    }

    override var size: Int
        get() = left.size + right.size
        set(size) {
            super.size = size
        }
    override var contents: Array<Item>
        get() {
            val contents = Array<Item>(left.size + right.size) { Item.create(ItemType.AIR) }
            System.arraycopy(left.contents, 0, contents, 0, left.size)
            System.arraycopy(right.contents, 0, contents, left.size, right.size)
            return contents
        }
        set(contents) {
            super.contents = contents
        }

    override fun onOpen(player: Player) {
        this.sendContents(player)
        if (viewer.size == 1) {
            val leftLocation = left.inventoryHolder.block.location
            val blockEventPacket = BlockEventPacket()
            blockEventPacket.blockPosition = leftLocation.toVector3i()
            blockEventPacket.eventType = 1
            blockEventPacket.eventData = 2
            player.world?.sendChunkPacket(leftLocation.blockX shr 4, leftLocation.blockZ shr 4, blockEventPacket)
            val rightLocation = right.inventoryHolder.block.location
            val blockEventPacket2 = BlockEventPacket()
            blockEventPacket2.blockPosition = rightLocation.toVector3i()
            blockEventPacket2.eventType = 1
            blockEventPacket2.eventData = 2
            player.world?.playSound(rightLocation, SoundEvent.CHEST_OPEN)
            player.world?.sendChunkPacket(rightLocation.blockX shr 4, rightLocation.blockZ shr 4, blockEventPacket2)
        }
    }

    override fun onClose(player: Player) {
        if (viewer.size == 0) {
            val leftLocation = left.inventoryHolder.block.location
            val blockEventPacket = BlockEventPacket()
            blockEventPacket.blockPosition = leftLocation.toVector3i()
            blockEventPacket.eventType = 1
            blockEventPacket.eventData = 0
            player.world?.sendChunkPacket(leftLocation.blockX shr 4, leftLocation.blockZ shr 4, blockEventPacket)
            val rightLocation = right.inventoryHolder.block.location
            val blockEventPacket2 = BlockEventPacket()
            blockEventPacket2.blockPosition = rightLocation.toVector3i()
            blockEventPacket2.eventType = 1
            blockEventPacket2.eventData = 0
            player.world?.playSound(rightLocation, SoundEvent.CHEST_CLOSED)
            player.world?.sendChunkPacket(rightLocation.blockX shr 4, rightLocation.blockZ shr 4, blockEventPacket2)
        }
    }
}
