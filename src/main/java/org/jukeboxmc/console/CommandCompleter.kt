package org.jukeboxmc.console

import org.jline.reader.Candidate
import org.jline.reader.Completer
import org.jline.reader.LineReader
import org.jline.reader.ParsedLine
import org.jukeboxmc.Server
import java.util.function.Consumer

/**
 * @author LucGamesYT
 * @version 1.0
 */
class CommandCompleter(server: Server) : Completer {
    override fun complete(lineReader: LineReader, parsedLine: ParsedLine, candidates: List<Candidate>) {
        // TODO
    }

    private fun addOptions(commandConsumer: Consumer<String>) {
        // TODO
    }

    val server: Server

    init {
        this.server = server
    }
}
