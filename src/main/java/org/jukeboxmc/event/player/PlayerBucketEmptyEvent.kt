package org.jukeboxmc.event.player

import org.jukeboxmc.block.Block
import org.jukeboxmc.item.Item
import org.jukeboxmc.player.Player

/**
 * @author Kaooot
 * @version 1.0
 */
class PlayerBucketEmptyEvent
/**
 * Creates a new [PlayerBucketEmptyEvent]
 *
 * @param player       who did an action with a bucket item
 * @param bucket       which represents the bucket item
 * @param itemInHand   which stands for the item which was hold by the player in their hand
 * @param clickedBlock which is the block the player clicked at
 * @param placedBlock  which is the block which will be placed
 */(
    player: Player,
    bucket: Item,
    itemInHand: Item,
    clickedBlock: Block?,
    var placedBlock: Block?,
) : PlayerBucketEvent(player, bucket, itemInHand, clickedBlock)
