package org.jukeboxmc.util

import java.util.concurrent.TimeUnit
import org.jukeboxmc.logger.Logger

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
        System.exit(1)
        logger.info("Server shutdown successfully!")
    }
}