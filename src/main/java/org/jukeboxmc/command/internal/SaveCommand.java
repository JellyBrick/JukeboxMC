package org.jukeboxmc.command.internal;

import org.jetbrains.annotations.NotNull;
import org.jukeboxmc.Server;
import org.jukeboxmc.command.Command;
import org.jukeboxmc.command.CommandData;
import org.jukeboxmc.command.CommandSender;
import org.jukeboxmc.command.annotation.Description;
import org.jukeboxmc.command.annotation.Name;
import org.jukeboxmc.command.annotation.Permission;
import org.jukeboxmc.world.World;

/**
 * @author LucGamesYT
 * @version 1.0
 */
@Name ("save")
@Description ("Save all worlds.")
@Permission("jukeboxmc.command.save")
public class SaveCommand extends Command {

    public SaveCommand() {
        super( CommandData.builder().build() );
    }

    @Override
    public void execute(@NotNull CommandSender commandSender, String command, String[] args ) {
        commandSender.sendMessage( "Saving all worlds..." );
        for ( World world : Server.getInstance().getWorlds() ) {
            world.save();
            commandSender.sendMessage( "Saving " + world.getName() + " success." );
        }
    }
}
