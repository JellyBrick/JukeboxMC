package org.jukeboxmc.logger

import lombok.extern.log4j.Log4j2
import org.apache.logging.log4j.LogManager

/**
 * @author LucGamesYT
 * @version 1.0
 */
@Log4j2
class Logger {
    private val log = LogManager.getLogger()
    fun info(message: String?) {
        log.info(message)
    }

    fun info(message: Any?) {
        log.info(message)
    }

    fun warn(message: Any?) {
        log.warn(message)
    }

    fun error(message: Any?) {
        log.error(message)
    }

    fun error(message: String?, throwable: Throwable?) {
        log.error(message, throwable)
    }

    fun debug(message: Any?) {
        log.debug(message)
    }

    companion object {
        val instance = Logger()
    }
}
