package org.jukeboxmc.console

import org.jukeboxmc.Server
import org.jukeboxmc.command.CommandSender

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ConsoleSender(override val server: Server) : CommandSender {

    override fun sendMessage(message: String?) {
        server.logger.info(message)
    }

    override fun hasPermission(permission: String): Boolean {
        return true
    }
}
