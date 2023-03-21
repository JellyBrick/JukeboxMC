package org.jukeboxmc.network.handler

import java.util.Locale
import org.jukeboxmc.Server
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class CommandRequestHandler : PacketHandler<CommandRequestPacket> {
    override fun handle(packet: CommandRequestPacket, server: Server, player: Player) {
        val commandParts: Array<String> =
            packet.getCommand().substring(1).split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
        val commandIdentifier = commandParts[0]
        val playerCommandPreprocessEvent = PlayerCommandPreprocessEvent(player, commandIdentifier)
        server.pluginManager.callEvent(playerCommandPreprocessEvent)
        if (playerCommandPreprocessEvent.isCancelled()) {
            return
        }
        server.pluginManager.commandManager.handleCommandInput(
            player,
            packet.getCommand().lowercase(Locale.getDefault())
        )
    }
}