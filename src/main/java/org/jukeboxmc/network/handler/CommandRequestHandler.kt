package org.jukeboxmc.network.handler

import com.nukkitx.protocol.bedrock.packet.CommandRequestPacket
import org.jukeboxmc.Server
import org.jukeboxmc.event.player.PlayerCommandPreprocessEvent
import org.jukeboxmc.player.Player
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class CommandRequestHandler : PacketHandler<CommandRequestPacket> {
    override fun handle(packet: CommandRequestPacket, server: Server, player: Player) {
        val commandParts: Array<String> =
            packet.command.substring(1).split(' ').dropLastWhile { it.isEmpty() }
                .toTypedArray()
        val commandIdentifier = commandParts[0]
        val playerCommandPreprocessEvent = PlayerCommandPreprocessEvent(player, commandIdentifier)
        server.pluginManager.callEvent(playerCommandPreprocessEvent)
        if (playerCommandPreprocessEvent.isCancelled) {
            return
        }
        server.pluginManager.getCommandManager().handleCommandInput(
            player,
            packet.command.lowercase(Locale.getDefault()),
        )
    }
}
