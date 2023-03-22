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
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.enchantment.EnchantmentType
import org.jukeboxmc.player.Player
import java.util.Locale
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * @author LucGamesYT
 * @version 1.0
 */
@Name("enchant")
@Description("Adds an enchantment to a player's selected item.")
@Permission("jukeboxmc.command.enchant")
class EnchantCommand : Command(
    CommandData.builder()
        .setParameters(
            arrayOf(
                CommandParameter("player", CommandParamType.TARGET, false),
                CommandParameter(
                    "enchantment",
                    Stream.of(*EnchantmentType.values())
                        .map { enchantmentType: EnchantmentType -> enchantmentType.name.lowercase(Locale.getDefault()) }
                        .collect(Collectors.toList()),
                    false,
                ),
                CommandParameter("level", CommandParamType.INT, true),
            ),
        )
        .build(),
) {
    override fun execute(commandSender: CommandSender, command: String, args: Array<String>) {
        if (commandSender is Player) {
            if (args.size >= 2) {
                val target: Player? = Server.instance.getPlayer(args[0])
                if (target == null) {
                    commandSender.sendMessage("§cThe player " + args[0] + " could not be found")
                    return
                }
                val enchantmentValue = args[1].lowercase(Locale.getDefault())
                if (!EnumUtils.isValidEnum(
                        EnchantmentType::class.java,
                        enchantmentValue.uppercase(Locale.getDefault()),
                    )
                ) {
                    commandSender.sendMessage("§cEnchantment not found.")
                    return
                }
                val enchantmentType = EnchantmentType.valueOf(enchantmentValue.uppercase(Locale.getDefault()))
                var level = 1
                if (args.size >= 3) {
                    if (!NumberUtils.isCreatable(args[2])) {
                        commandSender.sendMessage("§cThe level must be a number.")
                        return
                    }
                    level = args[2].toInt()
                }
                val itemInHand: Item = commandSender.inventory.itemInHand
                if (itemInHand.type == ItemType.AIR) {
                    commandSender.sendMessage("§cAir can not be enchanted.")
                    return
                }
                val enchantment = itemInHand.getEnchantment(enchantmentType)
                if (enchantment != null) {
                    commandSender.sendMessage("§cThe item already has the enchantment")
                    return
                }
                itemInHand.addEnchantment(enchantmentType, level)
                commandSender.inventory.itemInHand = itemInHand
                commandSender.sendMessage("Enchanting succeeded for " + target.name)
            }
        } else {
            commandSender.sendMessage("§cYou must be a player.")
        }
    }
}
