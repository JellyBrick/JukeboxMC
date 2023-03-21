package org.jukeboxmc.network.handler

import org.jukeboxmc.Server
import org.jukeboxmc.player.AdventureSettings
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class RequestAbilityHandler : PacketHandler<RequestAbilityPacket> {
    override fun handle(packet: RequestAbilityPacket, server: Server, player: Player) {
        if (!player.adventureSettings[AdventureSettings.Type.ALLOW_FLIGHT] && packet.getAbility() == Ability.FLYING) {
            player.adventureSettings[AdventureSettings.Type.FLYING] = false
            player.adventureSettings.update()
            return
        }
        if (player.adventureSettings[AdventureSettings.Type.ALLOW_FLIGHT] && packet.getAbility() == Ability.FLYING) {
            val playerToggleFlyEvent = PlayerToggleFlyEvent(player, packet.isBoolValue())
            playerToggleFlyEvent.setCancelled(!player.adventureSettings[AdventureSettings.Type.ALLOW_FLIGHT])
            server.pluginManager.callEvent(playerToggleFlyEvent)
            val playerAdventureSettings: AdventureSettings = player.adventureSettings
            playerAdventureSettings.set(
                AdventureSettings.Type.FLYING,
                if (playerToggleFlyEvent.isCancelled()) player.adventureSettings[AdventureSettings.Type.FLYING] else packet.isBoolValue()
            )
            playerAdventureSettings.update()
        }
    }
}