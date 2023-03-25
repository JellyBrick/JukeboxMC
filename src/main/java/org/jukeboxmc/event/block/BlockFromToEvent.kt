package org.jukeboxmc.event.block

import org.jukeboxmc.block.Block
import org.jukeboxmc.event.Cancellable

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockFromToEvent
/**
 * Creates a new [BlockFromToEvent]
 *
 * @param block   which represents the block from
 * @param blockTo which stands for the block which was changed
 */(
    block: Block,
    /**
     * Modifies the result block
     *
     * [blockTo] which should be modified
     */
    var blockTo: Block,
) : BlockEvent(block), Cancellable {
    /**
     * Retrieves the result bock which was affected with the event
     */
    val blockFrom: Block
        /**
         * Retrieves the original block
         *
         * @return a fresh [Block]
         */
        get() = super.block
}
