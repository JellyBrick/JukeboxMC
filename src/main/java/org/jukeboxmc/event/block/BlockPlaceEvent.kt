package org.jukeboxmc.event.block

import org.jukeboxmc.block.Block
import org.jukeboxmc.event.Cancellable
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockPlaceEvent
/**
 * Creates a new [BlockPlaceEvent]
 *
 * @param player        who placed a block
 * @param placedBlock   which should be placed
 * @param replacedBlock which represents the original block before the block place
 * @param clickedBlock  which represents the block the player clicked at
 */(
    /**
     * Retrieves the player who placed a block
     *
     * @return a fresh [Player]
     */
    val player: Player,
    /**
     * Modifies the block which should be placed
     *
     * [placedBlock] which should be placed
     */
    var placedBlock: Block,
    /**
     * Retrieves the block which was replaced
     *
     * [replacedBlock] a fresh [Block]
     */
    val replacedBlock: Block,
    /**
     * Retrieves the block the player clicked at
     *
     * [clickedBlock] [Block]
     */
    val clickedBlock: Block,
) : BlockEvent(placedBlock), Cancellable
