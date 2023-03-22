package org.jukeboxmc.command

import org.jukeboxmc.Server

/**
 * @author LucGamesYT
 * @version 1.0
 */
interface CommandSender {
    fun sendMessage(message: String)
    fun hasPermission(permission: String): Boolean
    val server: Server
}