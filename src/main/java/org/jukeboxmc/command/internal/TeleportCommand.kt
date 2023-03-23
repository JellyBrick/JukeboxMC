package org.jukeboxmc.command.internal

import org.cloudburstmc.protocol.bedrock.data.command.CommandParamType
import org.jukeboxmc.Server
import org.jukeboxmc.command.Command
import org.jukeboxmc.command.CommandData
import org.jukeboxmc.command.CommandParameter
import org.jukeboxmc.command.CommandSender
import org.jukeboxmc.command.annotation.Description
import org.jukeboxmc.command.annotation.Name
import org.jukeboxmc.command.annotation.Permission
import org.jukeboxmc.math.Location
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.world.Dimension
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
@Name("teleport")
@Description("Teleport to another location or player.")
@Permission("jukeboxmc.command.teleport")
class TeleportCommand : Command(
    CommandData.builder()
        .addAlias("tp")
        .setParameters(
            arrayOf(
                CommandParameter("player", CommandParamType.TARGET, false),
            ),
            arrayOf(
                CommandParameter("position", CommandParamType.POSITION, false),
                CommandParameter("dimension", mutableListOf("overworld", "nether", "the_end"), true),
            ),
            arrayOf(
                CommandParameter("player", CommandParamType.TARGET, false),
                CommandParameter("target", CommandParamType.TARGET, false),
            ),
        )
        .build(),
) {
    override fun execute(commandSender: CommandSender, command: String, args: Array<String>) {
        if (commandSender is Player) {
            when (args.size) {
                1 -> {
                    val targetName = args[0]
                    if (targetName.isEmpty()) {
                        commandSender.sendMessage("§cYou must specify a name")
                        return
                    }
                    val target: Player? = Server.instance.getPlayer(targetName)
                    if (target == null) {
                        commandSender.sendMessage("§cThe player $targetName could not be found")
                        return
                    }
                    commandSender.teleport(target)
                    commandSender.sendMessage("You have been teleported to $targetName")
                }
                2 -> {
                    val playerName = args[0]
                    val targetName = args[1]
                    if (playerName.isEmpty() || targetName.isEmpty()) {
                        commandSender.sendMessage("§cYou must specify a name for both players")
                        return
                    }
                    val tagetPlayer: Player? = Server.instance.getPlayer(playerName)
                    val toPlayer: Player? = Server.instance.getPlayer(targetName)
                    if (tagetPlayer == null) {
                        commandSender.sendMessage("§cPlayer $playerName could not be found")
                        return
                    }
                    if (toPlayer == null) {
                        commandSender.sendMessage("§cPlayer $targetName could not be found")
                        return
                    }
                    tagetPlayer.teleport(toPlayer)
                    commandSender.sendMessage("The player $playerName has been teleported to $targetName")
                }
                3, 4 -> {
                    val number1 = args[0]
                    val number2 = args[1]
                    val number3 = args[2]
                    var dimension: String? = null
                    if (args.size == 4) {
                        dimension = args[3]
                    }
                    if (number1.isEmpty() || number2.isEmpty() || number3.isEmpty()) {
                        commandSender.sendMessage("§cYou must specify a position")
                        return
                    }
                    try {
                        val x = number1.toInt()
                        val y = number2.toInt()
                        val z = number3.toInt()
                        commandSender.teleport(
                            Location(
                                commandSender.world,
                                Vector(x, y, z),
                                if (dimension == null) {
                                    commandSender.dimension
                                } else {
                                    Dimension.valueOf(
                                        dimension.uppercase(
                                            Locale.getDefault(),
                                        ),
                                    )
                                },
                            ),
                        )
                        commandSender.sendMessage("You have benn teleported to $x, $y, $z")
                    } catch (e: NumberFormatException) {
                        commandSender.sendMessage("§cYou must specify a number")
                    }
                }
            }
        } else {
            commandSender.sendMessage("§cYou must be a player")
        }
    }
}
