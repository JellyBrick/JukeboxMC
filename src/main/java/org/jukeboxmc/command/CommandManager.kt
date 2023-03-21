package org.jukeboxmc.command

import java.lang.reflect.InvocationTargetException
import java.util.Arrays
import org.jukeboxmc.command.internal.StatusCommand

/**
 * @author LucGamesYT
 * @version 1.0
 */
class CommandManager {
    private val commands: MutableList<Command> = ArrayList()

    init {
        val commands = Arrays.asList<Class<out Command>>(
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
            TimeCommand::class.java
        )
        for (commandClass in commands) {
            try {
                registerCommand(commandClass.getConstructor().newInstance())
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
        }
    }

    fun handleCommandInput(commandSender: CommandSender, input: String) {
        try {
            val commandParts = input.substring(1).split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val commandIdentifier = commandParts[0]
            var consumed = 0
            var targetCommand: Command? = null
            while (targetCommand == null) {
                for (command in commands) {
                    if (command.getCommandData().name.equals(
                            commandIdentifier,
                            ignoreCase = true
                        ) || command.getCommandData().aliases != null && command.getCommandData().aliases.contains(
                            commandIdentifier
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
            if (targetCommand.getCommandData().permission != null && !commandSender.hasPermission(targetCommand.getCommandData().permission)) {
                if (targetCommand.getCommandData().permissionMessage != null && !targetCommand.getCommandData().permissionMessage.isEmpty()) {
                    commandSender.sendMessage(targetCommand.getCommandData().permissionMessage)
                } else {
                    commandSender.sendMessage("§cYou don't have permission to do that")
                }
                return
            }
            val params: Array<String?>
            if (commandParts.size > consumed) {
                params = arrayOfNulls(commandParts.size - consumed)
                System.arraycopy(commandParts, consumed, params, 0, commandParts.size - consumed)
            } else {
                params = arrayOfNulls(0)
            }
            targetCommand.execute(commandSender, commandIdentifier, params)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun registerCommand(command: Command) {
        commands.add(command)
    }

    fun unregisterCommand(command: Command) {
        commands.remove(command)
    }

    fun isCommandRegistered(name: String?): Boolean {
        for (command in commands) {
            if (command.getCommandData().name.equals(name, ignoreCase = true)) {
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
            if (command.getCommandData().name.equals(
                    identifier,
                    ignoreCase = true
                ) || command.getCommandData().aliases != null && command.getCommandData().aliases.contains(identifier)
            ) {
                return command
            }
        }
        return null
    }
}