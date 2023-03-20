package org.jukeboxmc.network.handler;

import com.nukkitx.protocol.bedrock.packet.TextPacket;
import org.jetbrains.annotations.NotNull;
import org.jukeboxmc.Server;
import org.jukeboxmc.event.player.PlayerChatEvent;
import org.jukeboxmc.player.Player;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class TextHandler implements PacketHandler<TextPacket> {

    @Override
    public void handle(@NotNull TextPacket packet, @NotNull Server server, @NotNull Player player ) {
        if ( packet.getType().equals( TextPacket.Type.CHAT ) ) {
            PlayerChatEvent playerChatEvent = new PlayerChatEvent( player, "<" + player.getName() + "> ", packet.getMessage() );
            server.getPluginManager().callEvent( playerChatEvent );
            if ( playerChatEvent.isCancelled() ) {
                return;
            }
            for ( Player onlinePlayer : player.getServer().getOnlinePlayers() ) {
                onlinePlayer.sendMessage( playerChatEvent.getFormat() + playerChatEvent.getMessage() );
            }
            server.getLogger().info( playerChatEvent.getFormat() + playerChatEvent.getMessage() );
        }
    }
}
