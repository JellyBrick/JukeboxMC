package org.jukeboxmc.event.network

import com.nukkitx.protocol.bedrock.BedrockPacket
import org.jukeboxmc.event.Cancellable
import org.jukeboxmc.event.Event
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class PacketReceiveEvent(val player: Player, var packet: BedrockPacket) : Event(), Cancellable
