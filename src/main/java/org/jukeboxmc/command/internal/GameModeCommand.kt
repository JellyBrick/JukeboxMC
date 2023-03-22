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
import org.jukeboxmc.player.GameMode
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
@Name("gamemode")
@Description("Change your gamemode")
@Permission("jukeboxmc.command.gamemode")
class GameModeCommand : Command(
    CommandData.builder()
        .addAlias("gm")
        .setParameters(
            arrayOf(
                CommandParameter("gamemode", CommandParamType.INT, false),
                CommandParameter("target", CommandParamType.TARGET, true),
            ),
            arrayOf<CommandParameter>(
                CommandParameter("gamemode", mutableListOf("survival", "creative", "adventure", "spectator"), false),
                CommandParameter("target", CommandParamType.TARGET, true),
            ),
        )
        .build(),
) {
    override fun execute(commandSender: CommandSender, command: String, args: Array<String>) {
        if (commandSender is Player) {
            if (args.size == 1) {
                val gamemodeName = args[0]
                if (gamemodeName.isEmpty()) {
                    commandSender.sendMessage("§cYou must specify a gamemode")
                    return
                }
                val gameMode = getGameModeByName(gamemodeName)
                if (gameMode == null) {
                    commandSender.sendMessage("§cGamemode $gamemodeName not found.")
                    return
                }
                commandSender.gameMode = gameMode
                commandSender.sendMessage("Your game mode has been updated to " + gameMode.identifier)
            } else if (args.size == 2) {
                val gamemodeName = args[0]
                val targetName = args[1]
                if (gamemodeName.isEmpty()) {
                    commandSender.sendMessage("§cYou must specify a gamemode")
                    return
                }
                if (targetName.isEmpty()) {
                    commandSender.sendMessage("§cYou must specify a player")
                    return
                }
                val target: Player? = Server.instance.getPlayer(targetName)
                if (target == null) {
                    commandSender.sendMessage("§cThe player $targetName could not be found")
                    return
                }
                val gameMode = getGameModeByName(gamemodeName)
                if (gameMode == null) {
                    commandSender.sendMessage("§cGamemode $gamemodeName not found.")
                    return
                }
                target.gameMode = gameMode
                target.sendMessage("Your game mode has been updated to " + gameMode.identifier)
                if (target === commandSender) {
                    commandSender.sendMessage("Set own game mode to " + gameMode.identifier)
                } else {
                    commandSender.sendMessage("Set " + target.name + "'s game mode to " + gameMode.identifier)
                }
            } else {
                commandSender.sendMessage("§cUsage: /gamemode <gamemode> [target]")
            }
        }
    }

    private fun getGameModeByName(value: String): GameMode? {
        var gameMode: GameMode? = null
        when (value) {
            "survival", "0" -> gameMode = GameMode.SURVIVAL
            "creative", "1" -> gameMode = GameMode.CREATIVE
            "adventure", "2" -> gameMode = GameMode.ADVENTURE
            "spectator", "3" -> gameMode = GameMode.SPECTATOR
            else -> {}
        }
        return gameMode
    }
}
