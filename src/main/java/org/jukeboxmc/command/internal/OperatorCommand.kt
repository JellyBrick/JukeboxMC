package org.jukeboxmc.command.internal

import com.nukkitx.protocol.bedrock.data.command.CommandParamType
import org.jukeboxmc.Server
import org.jukeboxmc.command.Command
import org.jukeboxmc.command.CommandData
import org.jukeboxmc.command.CommandParameter
import org.jukeboxmc.command.CommandSender
import org.jukeboxmc.command.annotation.Description
import org.jukeboxmc.command.annotation.Name
import org.jukeboxmc.command.annotation.Permission
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
@Name("op")
@Description("Add a player to operator")
@Permission("jukeboxmc.command.operator")
class OperatorCommand : Command(
    CommandData.builder()
        .setParameters(
            arrayOf(
                CommandParameter("player", CommandParamType.TARGET, false),
            ),
        )
        .build(),
) {
    override fun execute(commandSender: CommandSender, command: String, args: Array<String>) {
        if (args.size == 1) {
            val playerName = args[0]
            if (playerName.isNotEmpty()) {
                val target: Player? = Server.instance.getPlayer(playerName)
                if (target != null) {
                    if (target.isOp) {
                        commandSender.sendMessage("The player $playerName is already an operator")
                        return
                    }
                    target.isOp = true
                    target.sendMessage("You are now operator")
                    Server.instance.addOperatorToFile(target.name)
                } else {
                    if (!Server.instance.isOperatorInFile(playerName)) {
                        Server.instance.addOperatorToFile(playerName)
                    } else {
                        commandSender.sendMessage("The player $playerName is already an operator")
                    }
                }
                commandSender.sendMessage("The player $playerName is now an operator")
            } else {
                commandSender.sendMessage("§cYou must specify a name")
            }
        }
    }
}
