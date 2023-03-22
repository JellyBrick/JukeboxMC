package org.jukeboxmc.event

/**
 * @author Kaooot
 * @version 1.0
 */
interface Cancellable {
    /**
     * Retrieves whether the [Cancellable] implementation is cancelled or not
     *
     * @return whether the event implementation is cancelled
     */
    /**
     * Updates the cancelled state to the given value
     *
     * @param cancelled which should be set
     */
    var isCancelled: Boolean
}
