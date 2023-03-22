package org.jukeboxmc.network.handler

import com.nukkitx.protocol.bedrock.packet.BlockPickRequestPacket
import org.jukeboxmc.Server
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.GameMode
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockPickRequestHandler : PacketHandler<BlockPickRequestPacket> {
    override fun handle(packet: BlockPickRequestPacket, server: Server, player: Player) {
        val position: Vector = Vector(packet.blockPosition)
        position.dimension = player.dimension
        player.world?.getBlock(position)?.let { pickedBlock ->
            if (player.gameMode == GameMode.CREATIVE) {
                val item = pickedBlock.toItem()
                if (item.type == ItemType.AIR) {
                    Server.instance.logger.warn("User try to pick air")
                    return
                }
                if (!player.inventory.contains(item)) {
                    player.inventory.addItem(item)
                }
            }
        }
    }
}