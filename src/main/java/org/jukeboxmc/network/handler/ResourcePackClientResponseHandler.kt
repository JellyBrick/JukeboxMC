package org.jukeboxmc.network.handler

import com.nukkitx.protocol.bedrock.data.ResourcePackType
import com.nukkitx.protocol.bedrock.packet.ResourcePackClientResponsePacket
import com.nukkitx.protocol.bedrock.packet.ResourcePackDataInfoPacket
import com.nukkitx.protocol.bedrock.packet.ResourcePackStackPacket
import org.jukeboxmc.Server
import org.jukeboxmc.player.Player
import org.jukeboxmc.resourcepack.ResourcePack

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ResourcePackClientResponseHandler : PacketHandler<ResourcePackClientResponsePacket> {
    override fun handle(packet: ResourcePackClientResponsePacket, server: Server, player: Player) {
        when (packet.status) {
            ResourcePackClientResponsePacket.Status.REFUSED -> player.playerConnection.disconnect("You have been disconnected.")
            ResourcePackClientResponsePacket.Status.SEND_PACKS -> {
                for (packIds in packet.packIds) {
                    val resourcePackEntryElements =
                        packIds.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val resourcePackEntryUuid = resourcePackEntryElements[0]
                    val resourcePack: ResourcePack? =
                        server.getResourcePackManager().retrieveResourcePackById(resourcePackEntryUuid)
                    if (resourcePack != null) {
                        val maxChunkSize = 1048576
                        val resourcePackDataInfoPacket = ResourcePackDataInfoPacket()
                        resourcePackDataInfoPacket.packId = resourcePack.getUuid()
                        resourcePackDataInfoPacket.packVersion = resourcePack.version
                        resourcePackDataInfoPacket.maxChunkSize = maxChunkSize.toLong()
                        resourcePackDataInfoPacket.chunkCount = (resourcePack.size / maxChunkSize).toInt().toLong()
                        resourcePackDataInfoPacket.compressedPackSize = resourcePack.size
                        resourcePackDataInfoPacket.hash = resourcePack.sha256
                        resourcePackDataInfoPacket.type = ResourcePackType.RESOURCE
                        player.playerConnection.sendPacket(resourcePackDataInfoPacket)
                    }
                }
            }

            ResourcePackClientResponsePacket.Status.HAVE_ALL_PACKS -> {
                val resourcePackStackPacket = ResourcePackStackPacket()
                for (pack in server.getResourcePackManager().retrieveResourcePacks()) {
                    resourcePackStackPacket.behaviorPacks
                        .add(ResourcePackStackPacket.Entry(pack.getUuid().toString(), pack.version, ""))
                }
                resourcePackStackPacket.gameVersion = "*"
                resourcePackStackPacket.isExperimentsPreviouslyToggled = false
                resourcePackStackPacket.isForcedToAccept = server.isForceResourcePacks
                player.playerConnection.sendPacket(resourcePackStackPacket)
            }

            ResourcePackClientResponsePacket.Status.COMPLETED -> player.playerConnection.initializePlayer()
            else -> {}
        }
    }
}
