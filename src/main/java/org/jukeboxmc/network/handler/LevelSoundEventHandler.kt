package org.jukeboxmc.network.handler

import org.cloudburstmc.protocol.bedrock.data.SoundEvent
import org.cloudburstmc.protocol.bedrock.packet.LevelSoundEventPacket
import org.jukeboxmc.Server
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class LevelSoundEventHandler : PacketHandler<LevelSoundEventPacket> {
    override fun handle(packet: LevelSoundEventPacket, server: Server, player: Player) {
        when (packet.sound) {
            SoundEvent.LAND, SoundEvent.ATTACK_NODAMAGE, SoundEvent.FALL, SoundEvent.HIT, SoundEvent.ATTACK_STRONG -> player.world.sendChunkPacket(
                player.chunkX,
                player.chunkZ,
                packet,
            )

            else -> {}
        }
    }
}
