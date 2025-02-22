package org.jukeboxmc.network.handler

import com.nukkitx.protocol.bedrock.packet.ResourcePackChunkDataPacket
import com.nukkitx.protocol.bedrock.packet.ResourcePackChunkRequestPacket
import org.jukeboxmc.Server
import org.jukeboxmc.player.Player
import org.jukeboxmc.resourcepack.ResourcePack

/**
 * @author pooooooon
 * @version 1.0
 */
class ResourcePackChunkRequestHandler : PacketHandler<ResourcePackChunkRequestPacket> {
    override fun handle(packet: ResourcePackChunkRequestPacket, server: Server, player: Player) {
        val resourcePackEntryElements: Array<String> =
            packet.packId.toString().split("_".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
        val resourcePackEntryUuid = resourcePackEntryElements[0]
        val resourcePack: ResourcePack? = server.getResourcePackManager().retrieveResourcePackById(resourcePackEntryUuid)
        if (resourcePack != null) {
            val resourcePackChunkDataPacket = ResourcePackChunkDataPacket()
            resourcePackChunkDataPacket.packId = packet.packId
            resourcePackChunkDataPacket.packVersion = packet.packVersion
            resourcePackChunkDataPacket.chunkIndex = packet.chunkIndex
            resourcePackChunkDataPacket.data = resourcePack.getChunk(1048576 * packet.chunkIndex, 1048576)
            resourcePackChunkDataPacket.progress = 1048576L * packet.chunkIndex
            player.playerConnection.sendPacketImmediately(resourcePackChunkDataPacket)
        }
    }
}
