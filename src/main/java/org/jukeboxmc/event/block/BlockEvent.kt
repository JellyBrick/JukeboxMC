package org.jukeboxmc.event.block

import org.jukeboxmc.block.Block
import org.jukeboxmc.event.Event

/**
 * @author Kaooot
 * @version 1.0
 */
abstract class BlockEvent
/**
 * Creates a new [BlockEvent]
 *
 * @param block which represents the block which comes with this event
 */(
    /**
     * Retrives the [Block] which comes with this [BlockEvent]
     *
     * @return a fresh [Block]
     */
    val block: Block
) : Event()