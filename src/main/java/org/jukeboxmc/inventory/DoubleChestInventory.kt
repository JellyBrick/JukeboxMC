package org.jukeboxmc.inventory

import com.nukkitx.protocol.bedrock.data.SoundEvent
import com.nukkitx.protocol.bedrock.data.inventory.ContainerType
import org.jukeboxmc.blockentity.BlockEntityChest
import org.jukeboxmc.item.Item
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class DoubleChestInventory(
    holder: InventoryHolder?,
    private val left: ChestInventory,
    private val right: ChestInventory
) : ChestInventory(holder, left.getSize() + right.getSize()), InventoryHolder {
    override fun clear() {
        right.clear()
        left.clear()
    }

    override val inventoryHolder: InventoryHolder?
        get() = holder as BlockEntityChest
    override val type: InventoryType
        get() = InventoryType.DOUBLE_CHEST
    override val windowTypeId: ContainerType
        get() = ContainerType.CONTAINER

    override fun setItem(slot: Int, item: Item, sendContent: Boolean) {
        if (slot < left.getSize()) {
            left.setItem(slot, item)
            if (sendContent) {
                for (player in viewer) {
                    this.sendContents(slot, player!!)
                }
            }
            return
        }
        right.setItem(slot - left.getSize(), item)
        if (sendContent) {
            for (player in viewer) {
                this.sendContents(slot, player!!)
            }
        }
    }

    override fun getItem(slot: Int): Item? {
        return if (slot < left.getSize()) left.getItem(slot) else right.getItem(slot - left.getSize())
    }

    override var size: Int
        get() = left.getSize() + right.getSize()
        set(size) {
            super.size = size
        }
    override var contents: Array<Item?>
        get() {
            val contents = arrayOfNulls<Item>(left.getSize() + right.getSize())
            System.arraycopy(left.contents, 0, contents, 0, left.getSize())
            System.arraycopy(right.contents, 0, contents, left.getSize(), right.getSize())
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
            blockEventPacket.setBlockPosition(leftLocation!!.toVector3i())
            blockEventPacket.setEventType(1)
            blockEventPacket.setEventData(2)
            player.world.sendChunkPacket(leftLocation.blockX shr 4, leftLocation.blockZ shr 4, blockEventPacket)
            val rightLocation = right.inventoryHolder.block.location
            val blockEventPacket2 = BlockEventPacket()
            blockEventPacket2.setBlockPosition(rightLocation!!.toVector3i())
            blockEventPacket2.setEventType(1)
            blockEventPacket2.setEventData(2)
            player.world.playSound(rightLocation, SoundEvent.CHEST_OPEN)
            player.world.sendChunkPacket(rightLocation.blockX shr 4, rightLocation.blockZ shr 4, blockEventPacket2)
        }
    }

    override fun onClose(player: Player) {
        if (viewer.size == 0) {
            val leftLocation = left.inventoryHolder.block.location
            val blockEventPacket = BlockEventPacket()
            blockEventPacket.setBlockPosition(leftLocation!!.toVector3i())
            blockEventPacket.setEventType(1)
            blockEventPacket.setEventData(0)
            player.world.sendChunkPacket(leftLocation.blockX shr 4, leftLocation.blockZ shr 4, blockEventPacket)
            val rightLocation = right.inventoryHolder.block.location
            val blockEventPacket2 = BlockEventPacket()
            blockEventPacket2.setBlockPosition(rightLocation!!.toVector3i())
            blockEventPacket2.setEventType(1)
            blockEventPacket2.setEventData(0)
            player.world.playSound(rightLocation, SoundEvent.CHEST_CLOSED)
            player.world.sendChunkPacket(rightLocation.blockX shr 4, rightLocation.blockZ shr 4, blockEventPacket2)
        }
    }
}