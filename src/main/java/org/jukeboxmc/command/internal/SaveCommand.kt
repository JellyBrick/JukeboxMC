package org.jukeboxmc.command.internal

import org.jukeboxmc.Server
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
@Name("save")
@Description("Save all worlds.")
@Permission("jukeboxmc.command.save")
class SaveCommand : Command(CommandData.Companion.builder().build()) {
    override fun execute(commandSender: CommandSender, command: String?, args: Array<String?>) {
        commandSender.sendMessage("Saving all worlds...")
        for (world in Server.Companion.getInstance().getWorlds()) {
            world.save()
            commandSender.sendMessage("Saving " + world.name + " success.")
        }
    }
}