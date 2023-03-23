package org.jukeboxmc.network.handler

import org.cloudburstmc.protocol.bedrock.data.Ability
import org.cloudburstmc.protocol.bedrock.packet.RequestAbilityPacket
import org.jukeboxmc.Server
import org.jukeboxmc.event.player.PlayerToggleFlyEvent
import org.jukeboxmc.player.AdventureSettings
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class RequestAbilityHandler : PacketHandler<RequestAbilityPacket> {
    override fun handle(packet: RequestAbilityPacket, server: Server, player: Player) {
        if (!player.adventureSettings[AdventureSettings.Type.ALLOW_FLIGHT] && packet.ability == Ability.FLYING) {
            player.adventureSettings[AdventureSettings.Type.FLYING] = false
            player.adventureSettings.update()
            return
        }
        if (player.adventureSettings[AdventureSettings.Type.ALLOW_FLIGHT] && packet.ability == Ability.FLYING) {
            val playerToggleFlyEvent = PlayerToggleFlyEvent(player, packet.isBoolValue)
            playerToggleFlyEvent.isCancelled = (!player.adventureSettings[AdventureSettings.Type.ALLOW_FLIGHT])
            server.pluginManager.callEvent(playerToggleFlyEvent)
            val playerAdventureSettings: AdventureSettings = player.adventureSettings
            playerAdventureSettings[AdventureSettings.Type.FLYING] = if (playerToggleFlyEvent.isCancelled) player.adventureSettings[AdventureSettings.Type.FLYING] else packet.isBoolValue
            playerAdventureSettings.update()
        }
    }
}
