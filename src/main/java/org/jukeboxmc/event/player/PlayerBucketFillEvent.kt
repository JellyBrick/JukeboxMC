package org.jukeboxmc.event.player

import org.jukeboxmc.block.Block
import org.jukeboxmc.item.Item
import org.jukeboxmc.player.Player

/**
 * @author Kaooot
 * @version 1.0
 */
class PlayerBucketFillEvent(player: Player, bucket: Item, itemInHand: Item, clickedBlock: Block?) :
    PlayerBucketEvent(player, bucket, itemInHand, clickedBlock)