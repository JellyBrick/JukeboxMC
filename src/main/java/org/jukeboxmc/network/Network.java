package org.jukeboxmc.network;

import com.nukkitx.protocol.bedrock.*;
import com.nukkitx.protocol.bedrock.v575.Bedrock_v575;
import org.jetbrains.annotations.NotNull;
import org.jukeboxmc.Server;
import org.jukeboxmc.player.PlayerConnection;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author LucGamesYT
 * @version 1.0
 */
public class Network implements BedrockServerEventHandler {

    public static final BedrockPacketCodec CODEC = Bedrock_v575.V575_CODEC;

    private final Server server;
    private final InetSocketAddress inetSocketAddress;
    private final @NotNull BedrockPong bedrockPong;
    private final @NotNull BedrockServer bedrockServer;

    private final Set<PlayerConnection> connections = new HashSet<>();

    private final @NotNull Predicate<PlayerConnection> removePredicate;
    private final @NotNull Consumer<PlayerConnection> updater;

    public Network( Server server, InetSocketAddress inetSocketAddress ) {
        this.server = server;
        this.inetSocketAddress = inetSocketAddress;
        this.bedrockPong = new BedrockPong();
        this.bedrockServer = new BedrockServer( inetSocketAddress );
        this.bedrockServer.setHandler( this );

        this.removePredicate = PlayerConnection::isClosed;
        this.updater = PlayerConnection::update;

        try {
            this.bedrockServer.bind().join();
            this.server.getLogger().info( "Server started successfully at " + this.inetSocketAddress.getHostString() + ":" + this.inetSocketAddress.getPort() + "!" );
        } catch ( Exception e ) {
            this.server.getLogger().error( "Could not start server! Is there already running something on this port?", e );
        }
    }

    @Override
    public BedrockPong onQuery( @NotNull InetSocketAddress inetSocketAddress ) {
        this.bedrockPong.setEdition( "MCPE" );
        this.bedrockPong.setGameType( this.server.getGameMode().getIdentifier() );
        this.bedrockPong.setMotd( this.server.getMotd() );
        this.bedrockPong.setSubMotd( this.server.getSubMotd() );
        this.bedrockPong.setPlayerCount( this.server.getOnlinePlayers().size() );
        this.bedrockPong.setMaximumPlayerCount( this.server.getMaxPlayers() );
        this.bedrockPong.setIpv4Port( this.inetSocketAddress.getPort() );
        this.bedrockPong.setNintendoLimited( false );
        this.bedrockPong.setProtocolVersion( CODEC.getProtocolVersion() );
        this.bedrockPong.setVersion( CODEC.getMinecraftVersion() );
        return this.bedrockPong;
    }

    @Override
    public boolean onConnectionRequest( @NotNull InetSocketAddress address ) {
        return this.server.getFinishedState().get() && this.server.getRunningState().get();
    }

    @Override
    public void onSessionCreation( @NotNull BedrockServerSession bedrockServerSession ) {
        try {
            this.server.addPlayer( this.addPlayer( new PlayerConnection( this.server, bedrockServerSession ) ).getPlayer() );
        } catch ( Throwable e ) {
            e.printStackTrace();
        }
    }

    private synchronized PlayerConnection addPlayer( PlayerConnection playerConnection ) {
        this.connections.add( playerConnection );
        return playerConnection;
    }

    public synchronized void update() {
        this.connections.removeIf( this.removePredicate );
        this.connections.forEach( this.updater );
    }

    public @NotNull BedrockServer getBedrockServer() {
        return this.bedrockServer;
    }

    public Server getServer() {
        return this.server;
    }
}
