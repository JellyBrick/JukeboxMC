package org.jukeboxmc.event.player

import org.jukeboxmc.block.Block
import org.jukeboxmc.event.Cancellable
import org.jukeboxmc.item.Item
import org.jukeboxmc.player.Player

/**
 * @author Kaooot
 * @version 1.0
 */
abstract class PlayerBucketEvent
/**
 * Creates a new [PlayerBucketEvent]
 *
 * @param player       who did an action with a bucket item
 * @param bucket       which represents the bucket item
 * @param itemInHand   which stands for the item which was hold by the player in their hand
 * @param clickedBlock which is the block the player clicked at
 */(
    player: Player,
    /**
     * Retrieves the item which was hold in the players hand
     * after the regular bucket item has updated
     *
     * @return a fresh [Item]
     */
    val bucket: Item,
    /**
     * Retrieves the item which was hold in the players hand
     *
     * @return a fresh [Item]
     */
    val itemInHand: Item,
    /**
     * Modifies the block the player clicked at
     *
     * [clickedBlock] which will be changed
     */
    var clickedBlock: Block?,
) : PlayerEvent(player), Cancellable
