package org.jukeboxmc.command.internal

import java.util.concurrent.ThreadLocalRandom
import java.util.stream.Collectors
import org.jukeboxmc.Server
import org.jukeboxmc.command.Command
import org.jukeboxmc.command.CommandData
import org.jukeboxmc.command.CommandSender
import org.jukeboxmc.command.annotation.Description
import org.jukeboxmc.command.annotation.Name
import org.jukeboxmc.command.annotation.Permission
import org.jukeboxmc.entity.Entity
import org.jukeboxmc.player.GameMode
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
@Name("kill")
@Description("Kills entities (players, mobs, etc.).")
@Permission("jukeboxmc.command.kill")
class KillCommand : Command(
    CommandData.Companion.builder()
        .setParameters(
            *arrayOf<CommandParameter>(
                CommandParameter("target", CommandParamType.TARGET, false)
            )
        )
        .build()
) {
    override fun execute(commandSender: CommandSender, command: String?, args: Array<String?>) {
        if (args.size == 1) {
            val target = args[0]
            if (target.equals("@a", ignoreCase = true)) {
                val builder = StringBuilder()
                for (player in Server.Companion.getInstance().getOnlinePlayers()) {
                    if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
                        player!!.kill()
                        builder.append(player!!.name).append(", ")
                    }
                }
                if (builder.length > 0) {
                    builder.setLength(builder.length - 2)
                    commandSender.sendMessage("$builder killed.")
                }
            } else if (target.equals("@e", ignoreCase = true)) {
                val entities: MutableList<Entity?> = ArrayList()
                for (world in Server.Companion.getInstance().getWorlds()) {
                    entities.addAll(world.entitys.stream().filter { entity: Entity? -> entity !is Player }
                        .collect<Set<Entity?>, Any>(Collectors.toSet<Entity?>()))
                }
                val builder = StringBuilder()
                for (entity in entities) {
                    entity!!.close()
                    builder.append(entity.name).append(", ")
                }
                if (builder.length > 0) {
                    builder.setLength(builder.length - 2)
                    commandSender.sendMessage("$builder killed.")
                }
            } else if (target.equals("@r", ignoreCase = true)) {
                val onlinePlayers: Collection<Player?> = Server.Companion.getInstance().getOnlinePlayers()
                val current = ThreadLocalRandom.current()
                val targetPlayer = ArrayList(onlinePlayers)[current.nextInt(onlinePlayers.size - 1)]
                if (targetPlayer != null) {
                    if (targetPlayer.gameMode != GameMode.CREATIVE) {
                        targetPlayer.kill()
                        commandSender.sendMessage("Killed " + targetPlayer.name)
                    }
                }
            } else if (target.equals(" @s", ignoreCase = true)) {
                if (commandSender is Player) {
                    if (commandSender.gameMode != GameMode.CREATIVE && commandSender.gameMode != GameMode.SPECTATOR) {
                        commandSender.kill()
                        commandSender.sendMessage(commandSender.name + " killed.")
                    }
                }
            } else {
                val targetPlayer: Player = Server.Companion.getInstance().getPlayer(target)
                if (targetPlayer != null) {
                    targetPlayer.kill()
                    commandSender.sendMessage(targetPlayer.name + " killed.")
                } else {
                    commandSender.sendMessage("Target not found.")
                }
            }
        } else {
            commandSender.sendMessage("Target not found.")
        }
    }
}