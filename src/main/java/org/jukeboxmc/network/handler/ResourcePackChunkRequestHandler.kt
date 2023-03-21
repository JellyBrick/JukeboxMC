package org.jukeboxmc.network.handler

import org.jukeboxmc.Server
import org.jukeboxmc.player.Player

/**
 * @author pooooooon
 * @version 1.0
 */
class ResourcePackChunkRequestHandler : PacketHandler<ResourcePackChunkRequestPacket> {
    override fun handle(packet: ResourcePackChunkRequestPacket, server: Server, player: Player) {
        val resourcePackEntryElements: Array<String> =
            packet.getPackId().toString().split("_".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
        val resourcePackEntryUuid = resourcePackEntryElements[0]
        val resourcePack: ResourcePack? = server.resourcePackManager.retrieveResourcePackById(resourcePackEntryUuid)
        if (resourcePack != null) {
            val resourcePackChunkDataPacket = ResourcePackChunkDataPacket()
            resourcePackChunkDataPacket.setPackId(packet.getPackId())
            resourcePackChunkDataPacket.setPackVersion(packet.getPackVersion())
            resourcePackChunkDataPacket.setChunkIndex(packet.getChunkIndex())
            resourcePackChunkDataPacket.setData(resourcePack.getChunk(1048576 * packet.getChunkIndex(), 1048576))
            resourcePackChunkDataPacket.setProgress(1048576L * packet.getChunkIndex())
            player.playerConnection.sendPacketImmediately(resourcePackChunkDataPacket)
        }
    }
}