package org.jukeboxmc.inventory

import com.nukkitx.protocol.bedrock.data.SoundEvent
import com.nukkitx.protocol.bedrock.data.inventory.ContainerType
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class EnderChestInventory(holder: InventoryHolder?) : ContainerInventory(holder, -1, 27) {
    var position: Vector? = null
    override val inventoryHolder: InventoryHolder?
        get() = holder as Player
    override val type: InventoryType
        get() = InventoryType.ENDER_CHEST
    override val windowTypeId: ContainerType
        get() = ContainerType.CONTAINER

    override fun onOpen(player: Player) {
        super.onOpen(player)
        if (viewer.size == 1) {
            val blockEventPacket = BlockEventPacket()
            blockEventPacket.setBlockPosition(position!!.toVector3i())
            blockEventPacket.setEventType(1)
            blockEventPacket.setEventData(2)
            player.world.playSound(position, SoundEvent.ENDERCHEST_OPEN)
            player.world.sendChunkPacket(position.getBlockX() shr 4, position.getBlockZ() shr 4, blockEventPacket)
        }
    }

    override fun onClose(player: Player) {
        if (viewer.size == 0) {
            val blockEventPacket = BlockEventPacket()
            blockEventPacket.setBlockPosition(position!!.toVector3i())
            blockEventPacket.setEventType(1)
            blockEventPacket.setEventData(0)
            player.world.playSound(position, SoundEvent.ENDERCHEST_CLOSED)
            player.world.sendChunkPacket(position.getBlockX() shr 4, position.getBlockZ() shr 4, blockEventPacket)
        }
    }
}