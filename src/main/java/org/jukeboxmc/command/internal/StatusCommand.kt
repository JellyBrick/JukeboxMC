package org.jukeboxmc.command.internal

import org.jukeboxmc.Server
import org.jukeboxmc.command.Command
import org.jukeboxmc.command.CommandData
import org.jukeboxmc.command.CommandSender
import org.jukeboxmc.command.annotation.Description
import org.jukeboxmc.command.annotation.Name
import org.jukeboxmc.util.Utils
import java.util.concurrent.TimeUnit

/**
 * @author LucGamesYT
 * @version 1.0
 */
@Name("status")
@Description("Show the current status form the server.")
class StatusCommand : Command(CommandData.builder().build()) {
    override fun execute(commandSender: CommandSender, command: String, args: Array<String>) {
        val builder = StringBuilder()
        builder.append("§7======== §8| §bServer Status §8| §7========").append("\n")
        builder.append("§7Uptime§8: §b")
            .append(formatUptime(System.currentTimeMillis() - Server.instance.startTime))
            .append("\n")
        val currentTps: Long = Server.instance.currentTps
        val tpsColor = if (currentTps < 17) "§e" else if (currentTps < 12) "§c" else "§a"
        builder.append("§7Current TPS§8: ").append(tpsColor).append(currentTps).append("\n")
        val runtime = Runtime.getRuntime()
        val totalMB = Utils.round(runtime.totalMemory().toDouble() / 1024 / 1024, 2)
        val usedMB = Utils.round((runtime.totalMemory() - runtime.freeMemory()).toDouble() / 1024 / 1024, 2)
        val maxMB = Utils.round(runtime.maxMemory().toDouble() / 1024 / 1024, 2)
        val usage = usedMB / maxMB * 100
        builder.append("§7Used VM Memory§8: §b").append(usedMB).append(" MB. (").append(Utils.round(usage, 2))
            .append("%)").append("\n")
        builder.append("§7Total VM Memory§8: §b").append(totalMB).append(" MB.")
        commandSender.sendMessage(builder.toString())
    }

    private fun formatUptime(uptime: Long): String {
        var uptime = uptime
        val days = TimeUnit.MILLISECONDS.toDays(uptime)
        uptime -= TimeUnit.DAYS.toMillis(days)
        val hours = TimeUnit.MILLISECONDS.toHours(uptime)
        uptime -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(uptime)
        uptime -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(uptime)
        return "$days §7Days §b$hours §7Hours §b$minutes §7Minutes §b$seconds §7Seconds"
    }
}
