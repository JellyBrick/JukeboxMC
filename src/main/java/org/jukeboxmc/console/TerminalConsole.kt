package org.jukeboxmc.console

import com.google.common.util.concurrent.ThreadFactoryBuilder
import java.util.function.Consumer
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jukeboxmc.Server
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class TerminalConsole(private val server: Server) : SimpleTerminalConsole() {
    private val executor: ExecutorService

    init {
        executor = Executors.newSingleThreadExecutor(
            ThreadFactoryBuilder().setNameFormat("JukeboxMC Terminal Console").build()
        )
    }

    protected override fun runCommand(command: String) {
        if (command == "checkexception") {
            val exception = Exception()
            exception.stackTrace = server.mainThread.stackTrace
            exception.printStackTrace(System.out)
        } else if (command.equals("test", ignoreCase = true)) {
            server.onlinePlayers.forEach(Consumer { player: Player? -> player!!.sendMessage("Hallo das ist ein Test!") })
        }
        if (isRunning) {
            server.scheduler.execute { server.dispatchCommand(server.consoleSender, command) }
        }
    }

    protected override fun shutdown() {
        executor.shutdownNow()
    }

    protected val isRunning: Boolean
        protected get() = server.runningState.get()

    protected override fun buildReader(builder: LineReaderBuilder): LineReader {
        builder.completer(CommandCompleter(server))
        builder.appName("JukeboxMC")
        builder.option(LineReader.Option.HISTORY_BEEP, false)
        builder.option(LineReader.Option.HISTORY_IGNORE_DUPS, true)
        builder.option(LineReader.Option.HISTORY_IGNORE_SPACE, true)
        return super.buildReader(builder)
    }

    override fun start() {
        super.start()
    }

    fun startConsole() {
        executor.execute(Runnable { start() })
    }

    fun stopConsole() {
        executor.shutdownNow()
    }
}