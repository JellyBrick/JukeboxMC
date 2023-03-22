package org.jukeboxmc.event.block

import org.jukeboxmc.block.Block
import org.jukeboxmc.entity.passiv.EntityFallingBlock
import org.jukeboxmc.event.Cancellable

/**
 * @author LucGamesYT
 * @version 1.0
 */
class FallingBlockEvent
/**
 * Creates a new [BlockEvent]
 *
 * @param block which represents the block which comes with this event
 */(block: Block, val entityFallingBlock: EntityFallingBlock) : BlockEvent(block), Cancellable
