package org.jukeboxmc.network.handler

import org.cloudburstmc.protocol.bedrock.packet.LoginPacket
import org.cloudburstmc.protocol.bedrock.packet.PlayStatusPacket
import org.cloudburstmc.protocol.bedrock.packet.ResourcePacksInfoPacket
import org.jukeboxmc.Server
import org.jukeboxmc.event.player.PlayerLoginEvent
import org.jukeboxmc.player.Player
import org.jukeboxmc.player.data.LoginData

/**
 * @author LucGamesYT
 * @version 1.0
 */
class LoginHandler : PacketHandler<LoginPacket> {
    override fun handle(packet: LoginPacket, server: Server, player: Player) {
        player.playerConnection.loginData = LoginData(packet)
        if (!player.playerConnection.loginData!!.isXboxAuthenticated && server.isOnlineMode) {
            player.kick("You must be logged in with your xbox account.")
            return
        }
        val playerLoginEvent = PlayerLoginEvent(player)
        if (Server.instance.onlinePlayers.size >= Server.instance
                .maxPlayers && !playerLoginEvent.canJoinFullServer()
        ) {
            playerLoginEvent.isCancelled = true
            playerLoginEvent.kickReason = ("Server is full.")
        }
        if (playerLoginEvent.isCancelled) {
            player.kick(playerLoginEvent.kickReason)
        }
        Server.instance.pluginManager.callEvent(playerLoginEvent)
        player.playerConnection.sendPlayStatus(PlayStatusPacket.Status.LOGIN_SUCCESS)
        val resourcePacksInfoPacket = ResourcePacksInfoPacket()
        for (resourcePack in server.getResourcePackManager().retrieveResourcePacks()) {
            resourcePacksInfoPacket.behaviorPackInfos.add(
                ResourcePacksInfoPacket.Entry(
                    resourcePack.getUuid().toString(),
                    resourcePack.version,
                    resourcePack.size,
                    "",
                    "",
                    resourcePack.getUuid().toString(),
                    false,
                    false,
                ),
            )
        }
        resourcePacksInfoPacket.isForcedToAccept = false
        resourcePacksInfoPacket.isForcingServerPacksEnabled = false
        resourcePacksInfoPacket.isScriptingEnabled = false
        player.playerConnection.sendPacket(resourcePacksInfoPacket)
    }
}
