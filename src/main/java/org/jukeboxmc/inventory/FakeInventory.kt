package org.jukeboxmc.inventory

import com.nukkitx.protocol.bedrock.packet.ContainerOpenPacket
import com.nukkitx.protocol.bedrock.packet.UpdateBlockPacket
import org.jukeboxmc.Server
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import java.util.concurrent.ConcurrentHashMap

/**
 * @author LucGamesYT
 * @version 1.0
 */
abstract class FakeInventory(holder: InventoryHolder?, holderId: Int, size: Int) :
    ContainerInventory(holder, holderId, size) {
    var customName: String? = null
    protected val openInventory: MutableMap<Player, FakeInventory> = ConcurrentHashMap<Player, FakeInventory>()
    protected val blockPositions: MutableMap<Player, List<Vector>> = HashMap()
    override fun addViewer(player: Player, position: Vector, windowId: Byte) {
        viewer.add(player)
        if (openInventory.put(player, this) != null) {
            return
        }
        val blockPosition = onOpenChest(player)
        blockPositions[player] = blockPosition
        if (blockPosition.isNotEmpty()) {
            val vector = blockPosition[0]
            val containerOpenPacket = ContainerOpenPacket()
            containerOpenPacket.id = windowId
            containerOpenPacket.uniqueEntityId = holderId
            containerOpenPacket.type = this.windowTypeId
            containerOpenPacket.blockPosition = vector.toVector3i()
            player.playerConnection.sendPacket(containerOpenPacket)
            this.sendContents(player)
        }
    }

    override fun removeViewer(player: Player) {
        super.removeViewer(player)
        val vectors = blockPositions[player]!!
        Server.instance.scheduler.scheduleDelayed(
            {
                for (position in vectors) {
                    val updateBlockPacket = UpdateBlockPacket()
                    updateBlockPacket.blockPosition = position.toVector3i()
                    updateBlockPacket.dataLayer = 0
                    updateBlockPacket.runtimeId = player.world!!.getBlock(position).runtimeId
                    updateBlockPacket.flags.addAll(UpdateBlockPacket.FLAG_ALL_PRIORITY)
                    player.playerConnection.sendPacket(updateBlockPacket)
                }
            },
            2,
        )
        openInventory.remove(player)
    }

    abstract fun onOpenChest(player: Player): List<Vector>
}
