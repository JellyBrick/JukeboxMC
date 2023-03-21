package org.jukeboxmc.command.internal

import org.jukeboxmc.Server
import org.jukeboxmc.command.Command
import org.jukeboxmc.command.CommandData
import org.jukeboxmc.command.CommandSender
import org.jukeboxmc.command.annotation.Description
import org.jukeboxmc.command.annotation.Name
import org.jukeboxmc.command.annotation.Permission
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
@Name("kick")
@Description("Kick a player with reason.")
@Permission("jukeboxmc.command.kick")
class KickCommand : Command(
    CommandData.Companion.builder()
        .setParameters(
            *arrayOf<CommandParameter>(
                CommandParameter("target", CommandParamType.TARGET, false),
                CommandParameter("reason", CommandParamType.TEXT, true)
            )
        )
        .build()
) {
    override fun execute(commandSender: CommandSender, command: String?, args: Array<String?>) {
        if (args.size == 1 || args.size == 2) {
            val target: Player = Server.Companion.getInstance().getPlayer(args[0])
            if (target != null) {
                val reason = if (args.size == 2) args[1] else "Kicked by admin."
                target.kick(reason)
            } else {
                commandSender.sendMessage("Target not found.")
            }
        } else {
            commandSender.sendMessage("Target not found.")
        }
    }
}