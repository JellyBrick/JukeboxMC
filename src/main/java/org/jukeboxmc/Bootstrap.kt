package org.jukeboxmc

import org.jukeboxmc.logger.Logger
import org.jukeboxmc.network.Network

/**
 * @author LucGamesYT
 * @version 1.0
 */
object Bootstrap {
    @JvmStatic
    fun main(args: Array<String>) {
        val logger = Logger()
        logger.info("Starting JukeboxMC (Bedrock Edition " + Network.Companion.CODEC.getMinecraftVersion() + " with Protocol " + Network.Companion.CODEC.getProtocolVersion() + ")")
        Server(logger)
    }
}