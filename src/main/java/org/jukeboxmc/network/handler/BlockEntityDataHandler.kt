package org.jukeboxmc.network.handler

import com.nukkitx.protocol.bedrock.packet.BlockEntityDataPacket
import org.jukeboxmc.Server
import org.jukeboxmc.blockentity.BlockEntitySign
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockEntityDataHandler : PacketHandler<BlockEntityDataPacket> {
    override fun handle(packet: BlockEntityDataPacket, server: Server, player: Player) {
        val vector = Vector(packet.blockPosition)
        vector.dimension = player.dimension
        player.world?.getBlock(vector)?.let { block ->
            if (block.blockEntity != null) {
                val blockEntity = block.blockEntity
                if (blockEntity is BlockEntitySign) {
                    blockEntity.updateBlockEntitySign(packet.data, player)
                }
            }
        }
    }
}
