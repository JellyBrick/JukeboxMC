package org.jukeboxmc.util

import org.jukeboxmc.logger.Logger
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ServerKiller(private val logger: Logger) : Thread() {
    override fun run() {
        try {
            sleep(TimeUnit.SECONDS.toMillis(3))
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
        logger.info("Server shutdown successfully!")
        exitProcess(1)
    }
}
