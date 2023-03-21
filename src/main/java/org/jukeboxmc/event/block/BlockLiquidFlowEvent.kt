package org.jukeboxmc.event.block

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.behavior.BlockLiquid
import org.jukeboxmc.event.Cancellable

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockLiquidFlowEvent
/**
 * Creates a new [BlockLiquidFlowEvent]
 *
 * @param blockTo      which is the block the liquid will flow into
 * @param source       represents the liquid source which caused the flow
 * @param newFlowDecay which stands for the flow decay
 */(
    /**
     * Retrieves the block the [BlockLiquid] will flow into
     *
     * @return a fresh [Block]
     */
    val blockTo: Block,
    /**
     * Retrieves the source which caused the flow
     *
     * @return a fresh [BlockLiquid]
     */
    val source: BlockLiquid,
    /**
     * Retrieves the new flow decay
     *
     * @return a fresh flow decay value
     */
    val newFlowDecay: Int
) : BlockEvent(blockTo), Cancellable