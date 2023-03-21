package org.jukeboxmc.event

/**
 * @author LucGamesYT
 * @version 1.0
 */
abstract class Event {
    var isCancelled = false
        get() {
            if (this !is Cancellable) {
                throw CancellableException()
            }
            return field
        }
        set(cancelled) {
            if (this !is Cancellable) {
                throw CancellableException()
            }
            field = cancelled
        }
}