package org.jukeboxmc.network.handler

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
        val vector: Vector = Vector(packet.getBlockPosition())
        vector.dimension = player.dimension
        val block = player.world.getBlock(vector)
        if (block != null && block.blockEntity != null) {
            if (block.blockEntity is BlockEntitySign) {
                blockEntitySign.updateBlockEntitySign(packet.getData(), player)
            }
        }
    }
}