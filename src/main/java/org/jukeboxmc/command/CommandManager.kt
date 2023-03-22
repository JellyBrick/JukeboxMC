package org.jukeboxmc.command

import org.jukeboxmc.command.internal.EffectCommand
import org.jukeboxmc.command.internal.EnchantCommand
import org.jukeboxmc.command.internal.GameModeCommand
import org.jukeboxmc.command.internal.GameRuleCommand
import org.jukeboxmc.command.internal.KickCommand
import org.jukeboxmc.command.internal.KillCommand
import org.jukeboxmc.command.internal.OperatorCommand
import org.jukeboxmc.command.internal.PluginsCommand
import org.jukeboxmc.command.internal.RemoveOperatorCommand
import org.jukeboxmc.command.internal.SaveCommand
import org.jukeboxmc.command.internal.StatusCommand
import org.jukeboxmc.command.internal.StopCommand
import org.jukeboxmc.command.internal.TeleportCommand
import org.jukeboxmc.command.internal.TimeCommand

/**
 * @author LucGamesYT
 * @version 1.0
 */
class CommandManager {
    private val commands: MutableList<Command> = ArrayList()

    init {
        val commands = listOf(
            PluginsCommand::class.java,
            GameModeCommand::class.java,
            OperatorCommand::class.java,
            RemoveOperatorCommand::class.java,
            TeleportCommand::class.java,
            StopCommand::class.java,
            SaveCommand::class.java,
            KillCommand::class.java,
            KickCommand::class.java,
            EffectCommand::class.java,
            EnchantCommand::class.java,
            GameRuleCommand::class.java,
            StatusCommand::class.java,
            TimeCommand::class.java,
        )
        commands.forEach { commandClass ->
            registerCommand(commandClass.getConstructor().newInstance())
        }
    }

    fun handleCommandInput(commandSender: CommandSender, input: String) {
        val commandParts = input.substring(1).split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val commandIdentifier = commandParts[0]
        var consumed = 0
        var targetCommand: Command? = null
        while (targetCommand == null) {
            for (command in commands) {
                if (command.commandData.name.equals(
                        commandIdentifier,
                        ignoreCase = true,
                    ) || command.commandData.getAliases().contains(
                        commandIdentifier,
                    )
                ) {
                    targetCommand = command
                    break
                }
            }
            consumed++
            if (targetCommand == null) {
                if (commandParts.size == consumed) {
                    break
                }
            }
        }
        if (targetCommand == null) {
            commandSender.sendMessage("The command for $commandIdentifier could not be found")
            return
        }
        if (targetCommand.commandData.getPermission() != null && !commandSender.hasPermission(targetCommand.commandData.getPermission())) {
            if (targetCommand.commandData.getPermissionMessage().isNotEmpty()) {
                commandSender.sendMessage(targetCommand.commandData.getPermissionMessage())
            } else {
                commandSender.sendMessage("§cYou don't have permission to do that")
            }
            return
        }
        val params: Array<String>
        if (commandParts.size > consumed) {
            params = Array(commandParts.size - consumed) { "" }
            System.arraycopy(commandParts, consumed, params, 0, commandParts.size - consumed)
        } else {
            params = emptyArray()
        }
        targetCommand.execute(commandSender, commandIdentifier, params)
    }

    fun registerCommand(command: Command) {
        commands.add(command)
    }

    fun unregisterCommand(command: Command) {
        commands.remove(command)
    }

    fun isCommandRegistered(name: String?): Boolean {
        for (command in commands) {
            if (command.commandData.name.equals(name, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    fun getCommands(): List<Command> {
        return ArrayList(commands)
    }

    private fun getCommandByNameOrAlias(identifier: String): Command? {
        for (command in commands) {
            if (command.commandData.name.equals(
                    identifier,
                    ignoreCase = true,
                ) || command.commandData.getAliases().contains(identifier)
            ) {
                return command
            }
        }
        return null
    }
}
