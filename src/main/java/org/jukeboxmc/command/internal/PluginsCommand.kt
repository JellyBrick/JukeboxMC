package org.jukeboxmc.command.internal

import org.jukeboxmc.Server
import org.jukeboxmc.command.Command
import org.jukeboxmc.command.CommandData
import org.jukeboxmc.command.CommandSender
import org.jukeboxmc.command.annotation.Description
import org.jukeboxmc.command.annotation.Name
import org.jukeboxmc.command.annotation.Permission
import org.jukeboxmc.plugin.Plugin

/**
 * @author LucGamesYT
 * @version 1.0
 */
@Name("plugins")
@Description("Show all enabled plugins.")
@Permission("jukeboxmc.command.plugins")
class PluginsCommand : Command(
    CommandData.builder()
        .addAlias("pl")
        .build(),
) {
    override fun execute(commandSender: CommandSender, command: String, args: Array<String>) {
        val plugins: Collection<Plugin?> = Server.instance.pluginManager.plugins
        if (plugins.isEmpty()) {
            commandSender.sendMessage("No plugins loaded.")
            return
        }
        val stringBuilder = StringBuilder("Plugins (" + plugins.size + "§r): ")
        for (plugin in plugins) {
            stringBuilder.append(if (plugin!!.isEnabled()) "§a" else "§c").append(plugin.name).append(" v")
                .append(plugin.version).append("§7, ")
        }
        stringBuilder.setLength(stringBuilder.length - 4)
        commandSender.sendMessage(stringBuilder.toString())
    }
}
