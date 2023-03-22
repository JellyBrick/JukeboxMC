package org.jukeboxmc.inventory

import com.nukkitx.protocol.bedrock.data.SoundEvent
import com.nukkitx.protocol.bedrock.data.inventory.ContainerType
import com.nukkitx.protocol.bedrock.packet.BlockEventPacket
import org.jukeboxmc.blockentity.BlockEntityChest
import org.jukeboxmc.math.Location
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
open class ChestInventory : ContainerInventory {
    constructor(holder: InventoryHolder?) : super(holder, -1, 27)
    constructor(holder: InventoryHolder?, size: Int) : super(holder, -1, size)

    override val inventoryHolder: BlockEntityChest
        get() = holder as BlockEntityChest
    override val type: InventoryType
        get() = InventoryType.CHEST
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
            player.world?.playSound(location, SoundEvent.CHEST_OPEN)
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
            player.world?.playSound(location, SoundEvent.CHEST_CLOSED)
            player.world?.sendChunkPacket(location.blockX shr 4, location.blockZ shr 4, blockEventPacket)
        }
    }
}
