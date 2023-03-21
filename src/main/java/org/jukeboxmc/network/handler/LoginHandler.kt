package org.jukeboxmc.network.handler

import org.jukeboxmc.Server
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class LoginHandler : PacketHandler<LoginPacket> {
    override fun handle(packet: LoginPacket, server: Server, player: Player) {
        player.playerConnection.setLoginData(LoginData(packet))
        if (!player.playerConnection.loginData.isXboxAuthenticated && server.isOnlineMode) {
            player.kick("You must be logged in with your xbox account.")
            return
        }
        val playerLoginEvent = PlayerLoginEvent(player)
        if (Server.Companion.getInstance().getOnlinePlayers().size >= Server.Companion.getInstance()
                .getMaxPlayers() && !playerLoginEvent.canJoinFullServer()
        ) {
            playerLoginEvent.setCancelled(true)
            playerLoginEvent.setKickReason("Server is full.")
        }
        if (playerLoginEvent.isCancelled()) {
            player.kick(playerLoginEvent.getKickReason())
        }
        Server.Companion.getInstance().getPluginManager().callEvent(playerLoginEvent)
        player.playerConnection.sendPlayStatus(PlayStatusPacket.Status.LOGIN_SUCCESS)
        val resourcePacksInfoPacket = ResourcePacksInfoPacket()
        for (resourcePack in server.resourcePackManager.retrieveResourcePacks()) {
            resourcePacksInfoPacket.getBehaviorPackInfos().add(
                ResourcePacksInfoPacket.Entry(
                    resourcePack.uuid.toString(),
                    resourcePack.version,
                    resourcePack.size,
                    "",
                    "",
                    resourcePack.uuid.toString(),
                    false,
                    false
                )
            )
        }
        resourcePacksInfoPacket.setForcedToAccept(false)
        resourcePacksInfoPacket.setForcingServerPacksEnabled(false)
        resourcePacksInfoPacket.setScriptingEnabled(false)
        player.playerConnection.sendPacket(resourcePacksInfoPacket)
    }
}