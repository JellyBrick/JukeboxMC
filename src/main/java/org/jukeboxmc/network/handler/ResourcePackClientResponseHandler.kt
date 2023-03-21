package org.jukeboxmc.network.handler

import org.jukeboxmc.Server
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ResourcePackClientResponseHandler : PacketHandler<ResourcePackClientResponsePacket> {
    override fun handle(packet: ResourcePackClientResponsePacket, server: Server, player: Player) {
        when (packet.getStatus()) {
            ResourcePackClientResponsePacket.Status.REFUSED -> player.playerConnection.disconnect("You have been disconnected.")
            ResourcePackClientResponsePacket.Status.SEND_PACKS -> {
                for (packIds in packet.getPackIds()) {
                    val resourcePackEntryElements =
                        packIds.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val resourcePackEntryUuid = resourcePackEntryElements[0]
                    val resourcePack: ResourcePack? =
                        server.resourcePackManager.retrieveResourcePackById(resourcePackEntryUuid)
                    if (resourcePack != null) {
                        val maxChunkSize = 1048576
                        val resourcePackDataInfoPacket = ResourcePackDataInfoPacket()
                        resourcePackDataInfoPacket.setPackId(resourcePack.getUuid())
                        resourcePackDataInfoPacket.setPackVersion(resourcePack.getVersion())
                        resourcePackDataInfoPacket.setMaxChunkSize(maxChunkSize.toLong())
                        resourcePackDataInfoPacket.setChunkCount(
                            (resourcePack.getSize() / maxChunkSize).toInt().toLong()
                        )
                        resourcePackDataInfoPacket.setCompressedPackSize(resourcePack.getSize())
                        resourcePackDataInfoPacket.setHash(resourcePack.getSha256())
                        resourcePackDataInfoPacket.setType(ResourcePackType.RESOURCE)
                        player.playerConnection.sendPacket(resourcePackDataInfoPacket)
                    }
                }
            }

            ResourcePackClientResponsePacket.Status.HAVE_ALL_PACKS -> {
                val resourcePackStackPacket = ResourcePackStackPacket()
                for (pack in server.resourcePackManager.retrieveResourcePacks()) {
                    resourcePackStackPacket.getBehaviorPacks()
                        .add(ResourcePackStackPacket.Entry(pack.uuid.toString(), pack.version, ""))
                }
                resourcePackStackPacket.setGameVersion("*")
                resourcePackStackPacket.setExperimentsPreviouslyToggled(false)
                resourcePackStackPacket.setForcedToAccept(server.isForceResourcePacks)
                player.playerConnection.sendPacket(resourcePackStackPacket)
            }

            ResourcePackClientResponsePacket.Status.COMPLETED -> player.playerConnection.initializePlayer()
        }
    }
}