package org.jukeboxmc.command.jukebox;

import org.jukeboxmc.command.Command;
import org.jukeboxmc.command.CommandData;
import org.jukeboxmc.command.CommandSender;
import org.jukeboxmc.command.annotation.Description;
import org.jukeboxmc.command.annotation.Name;
import org.jukeboxmc.entity.Entity;
import org.jukeboxmc.player.Player;

/**
 * @author LucGamesYT
 * @version 1.0
 */
@Name ( "kill" )
@Description ( "Kill a entity or player." )
public class KillCommand extends Command {

    public KillCommand() {
        super( CommandData.builder().build() );
    }

    @Override
    public void execute( CommandSender commandSender, String command, String[] args ) {
        if ( commandSender instanceof Player player ) {
            if ( args[0].equalsIgnoreCase( "@e" ) ) {
                for ( Entity entity : player.getWorld().getEntities() ) {
                    if ( !( entity instanceof Player ) ) {
                        entity.close();
                    }
                }
            }
        }
    }
}
