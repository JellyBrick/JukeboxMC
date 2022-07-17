package org.jukeboxmc.command.jukebox;

import com.nukkitx.protocol.bedrock.data.command.CommandParamType;
import org.jukeboxmc.JukeboxMC;
import org.jukeboxmc.command.Command;
import org.jukeboxmc.command.CommandData;
import org.jukeboxmc.command.CommandParameter;
import org.jukeboxmc.command.CommandSender;
import org.jukeboxmc.command.annotation.Description;
import org.jukeboxmc.command.annotation.Name;
import org.jukeboxmc.entity.Entity;
import org.jukeboxmc.entity.EntityLiving;
import org.jukeboxmc.player.GameMode;
import org.jukeboxmc.player.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * @author LucGamesYT
 * @version 1.0
 */
@Name ( "kill" )
@Description ( "Kills entities (players, mobs, etc.)." )
public class KillCommand extends Command {

    public KillCommand() {
        super( CommandData.builder()
                .setParameters( new CommandParameter[]{
                        new CommandParameter( "target", CommandParamType.TARGET, false )
                } )
                .build() );
    }

    @Override
    public void execute( CommandSender commandSender, String command, String[] args ) {
        if ( commandSender instanceof Player player ) {
            if ( args.length == 0 ) {
                player.sendMessage( "§cYou need a target." );
            } else if ( args.length == 1 ) {
                if ( args[0].equalsIgnoreCase( "@a" ) ) {
                    for ( Player target : player.getWorld().getPlayers() ) {
                        if ( !target.getGameMode().equals( GameMode.CREATIVE ) ) {
                            target.kill();
                        }
                    }
                } else if ( args[0].equalsIgnoreCase( "@e" ) ) {
                    StringBuilder builder = new StringBuilder( "Killed " );
                    for ( Entity entity : player.getWorld().getEntities() ) {
                        if ( !( entity instanceof Player ) && entity instanceof EntityLiving entityLiving ) {
                            builder.append( entity.getName() ).append( ", " );
                            entityLiving.kill();
                        }
                    }
                    builder.setLength( builder.length() - 2 );
                    player.sendMessage( builder.toString() );
                } else if ( args[0].equalsIgnoreCase( "@r" ) ) {
                    Collection<Player> onlinePlayers = JukeboxMC.getOnlinePlayers();
                    Random random = new Random();
                    Player target = new ArrayList<>( onlinePlayers ).get( random.nextInt( onlinePlayers.size() - 1 ) );
                    if ( target != null ) {
                        if ( !target.getGameMode().equals( GameMode.CREATIVE ) ) {
                            target.kill();
                            player.sendMessage( "Killed " + target.getName() );
                        }
                    }
                } else if ( args[0].equalsIgnoreCase( "@s" ) ) {
                    if ( !player.getGameMode().equals( GameMode.CREATIVE ) ) {
                        player.kill();
                        player.sendMessage( "Killed " + player.getName() );
                    }
                } else {
                    Player target = JukeboxMC.getPlayer( args[0] );
                    if ( target != null ) {
                        if ( !target.getGameMode().equals( GameMode.CREATIVE ) ) {
                            target.kill();
                            player.sendMessage( "Killed " + target.getName() );
                        }
                    } else {
                        player.sendMessage( "§cPlayer not found." );
                    }
                }
            }
        } else {
            commandSender.sendMessage( "§cYou must be a player." );
        }
    }
}
