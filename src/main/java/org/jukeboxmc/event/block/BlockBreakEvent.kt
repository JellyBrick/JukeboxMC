package org.jukeboxmc.event.block

import org.jukeboxmc.block.Block
import org.jukeboxmc.event.Cancellable
import org.jukeboxmc.item.Item
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockBreakEvent
/**
 * Creates a new [BlockBreakEvent]
 *
 * @param player who has broken the given block
 * @param block  which was broken by the player
 * @param drops  which are the drops of the block affected by the item which was used
 */(
    /**
     * Retrieves the player who comes with this event
     *
     * @return a fresh [Player]
     */
    val player: Player,
    /**
     * Modifies the block which was broken
     *
     * @param block which was broken
     */
    override var block: Block,
    /**
     * Modifies the drops of the block which was broken
     *
     * @param drops which should be updated
     */
    var drops: List<Item>
) : BlockEvent(block), Cancellable {

    /**
     * Retrieves the drops of the block
     *
     * @return a fresh [<]
     */

}