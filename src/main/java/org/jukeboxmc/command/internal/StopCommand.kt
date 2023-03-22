package org.jukeboxmc.command.internal

import org.jukeboxmc.command.Command
import org.jukeboxmc.command.CommandData
import org.jukeboxmc.command.CommandSender
import org.jukeboxmc.command.annotation.Description
import org.jukeboxmc.command.annotation.Name
import org.jukeboxmc.command.annotation.Permission

/**
 * @author LucGamesYT
 * @version 1.0
 */
@Name("stop")
@Description("Stop the server")
@Permission("jukeboxmc.command.stop")
class StopCommand : Command(CommandData.builder().build()) {
    override fun execute(commandSender: CommandSender, command: String, args: Array<String>) {
        commandSender.sendMessage("Stopping the server...")
        commandSender.server.shutdown()
    }
}
