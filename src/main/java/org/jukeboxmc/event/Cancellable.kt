package org.jukeboxmc.event

/**
 * @author Kaooot
 * @version 1.0
 */
interface Cancellable {
    /**
     * Retrieves whether the [Cancellable] implementation is cancelled or not,
     * and updates the cancelled state to the given value
     *
     * [isCancelled] which should be set
     */
    var isCancelled: Boolean
}
