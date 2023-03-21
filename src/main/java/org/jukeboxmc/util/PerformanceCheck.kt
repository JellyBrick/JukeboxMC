package org.jukeboxmc.util

import org.jukeboxmc.Server

/**
 * @author LucGamesYT
 * @version 1.0
 */
class PerformanceCheck {
    private val name: String
    private var start: Long = 0
    private var startAt = 5

    constructor(name: String) {
        this.name = name
    }

    constructor(name: String, startAt: Int) {
        this.name = name
        this.startAt = startAt
    }

    fun start() {
        start = System.currentTimeMillis()
    }

    fun stop() {
        val stop = System.currentTimeMillis()
        val result = stop - start
        if (result >= startAt) {
            Server.Companion.getInstance().getLogger()
                .info(name + " -> " + result + "ms Thread: " + Thread.currentThread().name)
        }
    }
}