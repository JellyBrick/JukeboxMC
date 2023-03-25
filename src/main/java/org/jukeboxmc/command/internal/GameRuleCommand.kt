package org.jukeboxmc.command.internal

import org.apache.commons.lang3.math.NumberUtils
import org.cloudburstmc.protocol.bedrock.data.command.CommandParamType
import org.jukeboxmc.command.Command
import org.jukeboxmc.command.CommandData
import org.jukeboxmc.command.CommandParameter
import org.jukeboxmc.command.CommandSender
import org.jukeboxmc.command.annotation.Description
import org.jukeboxmc.command.annotation.Name
import org.jukeboxmc.command.annotation.Permission
import org.jukeboxmc.player.Player
import org.jukeboxmc.world.gamerule.GameRule
import java.lang.Boolean
import java.util.Arrays
import java.util.Locale
import kotlin.Array
import kotlin.String
import kotlin.arrayOf

/**
 * @author LucGamesYT
 * @version 1.0
 */
@Name("gamerule")
@Description("Set a gamerule value")
@Permission("jukeboxmc.command.gamerule")
class GameRuleCommand : Command(
    CommandData.builder()
        .setParameters(
            arrayOf(
                CommandParameter(
                    "rule",
                    Arrays.stream(GameRule.values())
                        .map { gameRule: GameRule -> gameRule.identifier.lowercase(Locale.getDefault()) }
                        .toList(),
                    false,
                ),
                CommandParameter("value", mutableListOf("true", "false"), false),
            ),
            arrayOf(
                CommandParameter(
                    "rule",
                    Arrays.stream(GameRule.values())
                        .map { gameRule: GameRule -> gameRule.identifier.lowercase(Locale.getDefault()) }
                        .toList(),
                    false,
                ),
                CommandParameter("value", CommandParamType.INT, false),
            ),
        ).build(),
) {
    override fun execute(commandSender: CommandSender, command: String, args: Array<String>) {
        if (commandSender is Player) {
            if (args.size == 2) {
                val gameRuleValue = args[0]
                val gameRule: GameRule? = GameRule.fromIdentifier(gameRuleValue)
                if (gameRule != null) {
                    if (NumberUtils.isCreatable(args[1])) {
                        val value = args[1].toInt()
                        if (gameRule == GameRule.MAX_COMMAND_CHAIN_LENGTH || gameRule == GameRule.SPAWN_RADIUS) {
                            commandSender.world.setGameRule(gameRule, value)
                            commandSender.sendMessage("Gamerule " + gameRule.identifier + " has been updated to " + value)
                        } else {
                            commandSender.sendMessage("§cYou can only use one number for maxCommandChainLength or spawnRadius.")
                        }
                    } else {
                        val boolValue = args[1]
                        if (boolValue.equals("true", ignoreCase = true) || boolValue.equals(
                                "false",
                                ignoreCase = true,
                            )
                        ) {
                            val value = Boolean.parseBoolean(boolValue)
                            commandSender.world.setGameRule(gameRule, value)
                            commandSender.sendMessage("Gamerule " + gameRule.identifier + " has been updated to " + value)
                        } else {
                            commandSender.sendMessage("§cValue must be a boolean.")
                        }
                    }
                } else {
                    commandSender.sendMessage("§cGameRule not found.")
                }
            }
        } else {
            commandSender.sendMessage("§cYou must be a player.")
        }
    }
}
