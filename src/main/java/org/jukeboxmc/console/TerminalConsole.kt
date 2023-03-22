package org.jukeboxmc.console

import com.google.common.util.concurrent.ThreadFactoryBuilder
import net.minecrell.terminalconsole.SimpleTerminalConsole
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jukeboxmc.Server
import org.jukeboxmc.player.Player
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.function.Consumer

/**
 * @author LucGamesYT
 * @version 1.0
 */
open class TerminalConsole(private val server: Server) : SimpleTerminalConsole() {
    private val executor: ExecutorService = Executors.newSingleThreadExecutor(
        ThreadFactoryBuilder().setNameFormat("JukeboxMC Terminal Console").build(),
    )

    override fun isRunning(): Boolean {
        return server.getRunningState().get()
    }

    protected override fun runCommand(command: String) {
        if (command == "checkexception") {
            val exception = Exception()
            exception.stackTrace = server.getMainThread().stackTrace
            exception.printStackTrace(System.out)
        } else if (command.equals("test", ignoreCase = true)) {
            server.onlinePlayers.forEach(Consumer { player: Player? -> player!!.sendMessage("Hallo das ist ein Test!") })
        }
        if (isRunning()) {
            server.scheduler.execute { server.dispatchCommand(server.getConsoleSender(), command) }
        }
    }

    override fun shutdown() {
        executor.shutdownNow()
    }

    override fun buildReader(builder: LineReaderBuilder): LineReader {
        builder.completer(CommandCompleter(server))
        builder.appName("JukeboxMC")
        builder.option(LineReader.Option.HISTORY_BEEP, false)
        builder.option(LineReader.Option.HISTORY_IGNORE_DUPS, true)
        builder.option(LineReader.Option.HISTORY_IGNORE_SPACE, true)
        return super.buildReader(builder)
    }

    fun startConsole() {
        executor.execute(::start)
    }

    fun stopConsole() {
        executor.shutdownNow()
    }
}
