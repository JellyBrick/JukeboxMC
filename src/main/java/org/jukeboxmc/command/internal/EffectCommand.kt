package org.jukeboxmc.command.internal

import com.nukkitx.protocol.bedrock.data.command.CommandParamType
import org.apache.commons.lang3.EnumUtils
import org.apache.commons.lang3.math.NumberUtils
import org.jukeboxmc.Server
import org.jukeboxmc.command.Command
import org.jukeboxmc.command.CommandData
import org.jukeboxmc.command.CommandParameter
import org.jukeboxmc.command.CommandSender
import org.jukeboxmc.command.annotation.Description
import org.jukeboxmc.command.annotation.Name
import org.jukeboxmc.command.annotation.Permission
import org.jukeboxmc.player.Player
import org.jukeboxmc.potion.Effect
import org.jukeboxmc.potion.EffectType
import java.lang.Boolean
import java.util.Locale
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.Array
import kotlin.String
import kotlin.arrayOf

/**
 * @author LucGamesYT
 * @version 1.0
 */
@Name("effect")
@Description("Add or remove status effect.")
@Permission("jukeboxmc.command.effect")
class EffectCommand : Command(
    CommandData.builder()
        .setParameters(
            arrayOf<CommandParameter>(
                CommandParameter("target", CommandParamType.TARGET, false),
                CommandParameter(
                    "effect",
                    Stream.of(*EffectType.values())
                        .map { effectType: EffectType -> effectType.name.lowercase(Locale.getDefault()) }
                        .collect(Collectors.toList()),
                    false,
                ),
                CommandParameter("seconds", CommandParamType.INT, true),
                CommandParameter("amplifier", CommandParamType.INT, true),
                CommandParameter("visible", mutableListOf("true", "false"), true),
            ),
            arrayOf(
                CommandParameter("target", CommandParamType.TARGET, false),
                CommandParameter("clear", mutableListOf("clear"), false),
            ),
        )
        .build(),
) {
    override fun execute(commandSender: CommandSender, command: String, args: Array<String>) {
        if (args.size >= 2) {
            val target: Player? = Server.instance.getPlayer(args[0])
            val type = args[1]!!.lowercase(Locale.getDefault())
            if (target == null) {
                commandSender.sendMessage("§cThe player " + args[0] + " could not be found")
                return
            }
            if (type.equals("clear", ignoreCase = true)) {
                target.removeAllEffects()
                commandSender.sendMessage("Took all effects from " + target.name)
            } else {
                if (!EnumUtils.isValidEnum(EffectType::class.java, type.uppercase(Locale.getDefault()))) {
                    commandSender.sendMessage("Effect not found.")
                    return
                }
                val effectType = EffectType.valueOf(type.uppercase(Locale.getDefault()))
                if (args.size == 3) {
                    if (!NumberUtils.isCreatable(args[2])) {
                        commandSender.sendMessage("The seconds must be a number.")
                        return
                    }
                }
                val seconds = if (args.size >= 3) args[2]!!.toInt() else 30
                if (args.size == 4) {
                    if (!NumberUtils.isCreatable(args[3])) {
                        commandSender.sendMessage("The amplifier must be a number.")
                        return
                    }
                }
                val amplifier = if (args.size >= 4) args[3]!!.toInt() else 0
                if (args.size >= 5) {
                    if (!args[4].equals("true", ignoreCase = true) && !args[4].equals("false", ignoreCase = true)) {
                        commandSender.sendMessage("The visible must be a boolean.")
                        return
                    }
                }
                val visible = args.size != 5 || Boolean.parseBoolean(args[4])
                target.addEffect(
                    Effect.create<Effect>(effectType).setDuration(seconds, TimeUnit.SECONDS)
                        .setAmplifier(amplifier).setVisible(visible),
                )
                commandSender.sendMessage("Gave " + effectType.name.lowercase(Locale.getDefault()) + " * " + amplifier + " to " + target.name + " for " + seconds + " seconds.")
            }
        }
    }
}
