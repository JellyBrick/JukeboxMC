package org.jukeboxmc.event.server

import org.jukeboxmc.Server

/**
 * @author LucGamesYT
 * @version 1.0
 */
class TpsChangeEvent(server: Server, val lastTps: Long, val currentTps: Long) : ServerEvent(server)
