package org.jukeboxmc.inventory

import com.nukkitx.protocol.bedrock.data.SoundEvent
import com.nukkitx.protocol.bedrock.data.inventory.ContainerType
import com.nukkitx.protocol.bedrock.packet.BlockEventPacket
import org.jukeboxmc.blockentity.BlockEntityShulkerBox
import org.jukeboxmc.math.Location
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ShulkerBoxInventory(holder: InventoryHolder?) : ContainerInventory(holder, -1, 27) {
    override val inventoryHolder: BlockEntityShulkerBox
        get() = holder as BlockEntityShulkerBox
    override val type: InventoryType
        get() = InventoryType.SHULKER_BOX
    override val windowTypeId: ContainerType
        get() = ContainerType.CONTAINER

    override fun onOpen(player: Player) {
        super.onOpen(player)
        if (viewer.size == 1) {
            val location: Location = inventoryHolder.block.location
            val blockEventPacket = BlockEventPacket()
            blockEventPacket.blockPosition = location.toVector3i()
            blockEventPacket.eventType = 1
            blockEventPacket.eventData = 2
            player.world?.playSound(location, SoundEvent.SHULKERBOX_OPEN)
            player.world?.sendChunkPacket(location.blockX shr 4, location.blockZ shr 4, blockEventPacket)
        }
    }

    override fun onClose(player: Player) {
        if (viewer.size == 0) {
            val location: Location = inventoryHolder.block.location
            val blockEventPacket = BlockEventPacket()
            blockEventPacket.blockPosition = location.toVector3i()
            blockEventPacket.eventType = 1
            blockEventPacket.eventData = 0
            player.world?.playSound(location, SoundEvent.SHULKERBOX_CLOSED)
            player.world?.sendChunkPacket(location.blockX shr 4, location.blockZ shr 4, blockEventPacket)
        }
    }
}
