package org.jukeboxmc.inventory

import org.cloudburstmc.protocol.bedrock.data.SoundEvent
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerType
import org.cloudburstmc.protocol.bedrock.packet.BlockEventPacket
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class EnderChestInventory(holder: InventoryHolder?) : ContainerInventory(holder, -1, 27) {
    var position: Vector? = null
    override val inventoryHolder: Player
        get() = holder as Player
    override val type: InventoryType
        get() = InventoryType.ENDER_CHEST
    override val windowTypeId: ContainerType
        get() = ContainerType.CONTAINER

    override fun onOpen(player: Player) {
        super.onOpen(player)
        if (viewer.size == 1) {
            val blockEventPacket = BlockEventPacket()
            blockEventPacket.blockPosition = position!!.toVector3i()
            blockEventPacket.eventType = 1
            blockEventPacket.eventData = 2
            player.world?.playSound(position!!, SoundEvent.ENDERCHEST_OPEN)
            player.world?.sendChunkPacket(position!!.blockX shr 4, position!!.blockZ shr 4, blockEventPacket)
        }
    }

    override fun onClose(player: Player) {
        if (viewer.size == 0) {
            val blockEventPacket = BlockEventPacket()
            blockEventPacket.blockPosition = position!!.toVector3i()
            blockEventPacket.eventType = 1
            blockEventPacket.eventData = 0
            player.world?.playSound(position!!, SoundEvent.ENDERCHEST_CLOSED)
            player.world?.sendChunkPacket(position!!.blockX shr 4, position!!.blockZ shr 4, blockEventPacket)
        }
    }
}
