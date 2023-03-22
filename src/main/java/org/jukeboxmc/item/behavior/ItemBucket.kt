package org.jukeboxmc.item.behavior

import com.nukkitx.protocol.bedrock.data.SoundEvent
import org.jukeboxmc.Server
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockLava
import org.jukeboxmc.block.behavior.BlockLiquid
import org.jukeboxmc.block.behavior.Waterlogable
import org.jukeboxmc.event.player.PlayerBucketEmptyEvent
import org.jukeboxmc.event.player.PlayerBucketFillEvent
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.math.Location
import org.jukeboxmc.player.GameMode
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
open class ItemBucket : Item {
    constructor(identifier: Identifier) : super(identifier)
    constructor(itemType: ItemType) : super(itemType)

    override fun useOnBlock(player: Player, block: Block, placeLocation: Location): Boolean {
        var block = block
        if (block !is BlockLiquid && block.type != BlockType.POWDER_SNOW) {
            block = player.world?.getBlock(block.location, 1)!!
        }
        if (block is BlockLiquid || block.type == BlockType.POWDER_SNOW) {
            if (this.type != ItemType.BUCKET) {
                return false
            }
            val item: Item =
                if (block.type == BlockType.POWDER_SNOW) {
                    create(ItemType.POWDER_SNOW_BUCKET)
                } else if (block is BlockLava) {
                    create(
                        ItemType.LAVA_BUCKET,
                    )
                } else {
                    create(ItemType.WATER_BUCKET)
                }
            val playerBucketFillEvent = PlayerBucketFillEvent(player, this, item, block)
            Server.instance.pluginManager.callEvent(playerBucketFillEvent)
            if (playerBucketFillEvent.isCancelled) {
                player.inventory.sendContents(player)
                return false
            }
            player.world?.setBlock(block.location, Block.create(BlockType.AIR), 0)
            if (block.type == BlockType.POWDER_SNOW) {
                player.world?.playSound(player.location, SoundEvent.BUCKET_FILL_POWDER_SNOW)
            } else if (block is BlockLava) {
                player.world?.playSound(player.location, SoundEvent.BUCKET_FILL_LAVA)
            } else {
                player.world?.playSound(player.location, SoundEvent.BUCKET_FILL_WATER)
            }
            if (player.gameMode != GameMode.CREATIVE) {
                if (amount - 1 <= 0) {
                    player.inventory.itemInHand = playerBucketFillEvent.itemInHand
                } else {
                    val clone = clone()
                    clone.setAmount(amount - 1)
                    player.inventory.itemInHand = clone
                    if (!player.inventory.addItem(playerBucketFillEvent.itemInHand)) {
                        player.world?.dropItem(item, player.location, null)
                    }
                }
            }
        } else {
            block = block.world?.getBlock(block.location, 0)!!
            val placedBlock: Block?
            when (this.type) {
                ItemType.BUCKET, ItemType.MILK_BUCKET, ItemType.COD_BUCKET, ItemType.SALMON_BUCKET, ItemType.PUFFERFISH_BUCKET, ItemType.TROPICAL_FISH_BUCKET, ItemType.AXOLOTL_BUCKET -> {
                    return false
                }

                else -> {
                    placedBlock = toBlock()
                    if (block is Waterlogable && this.type == ItemType.WATER_BUCKET) {
                        placedBlock.location = block.location
                        placedBlock.layer = 1
                    } else if (block is BlockLiquid) {
                        return false
                    } else {
                        placedBlock.location = placeLocation
                    }
                }
            }
            val playerBucketEmptyEvent = PlayerBucketEmptyEvent(
                player,
                this,
                create(ItemType.BUCKET),
                block,
                placedBlock,
            )
            Server.instance.pluginManager.callEvent(playerBucketEmptyEvent)
            if (playerBucketEmptyEvent.isCancelled) {
                player.inventory.sendContents(player)
                return false
            }
            if (placedBlock.type == BlockType.POWDER_SNOW) {
                player.world?.playSound(player.location, SoundEvent.BUCKET_EMPTY_POWDER_SNOW)
            } else if (placedBlock is BlockLava) {
                player.world?.playSound(player.location, SoundEvent.BUCKET_EMPTY_LAVA)
            } else {
                player.world?.playSound(player.location, SoundEvent.BUCKET_EMPTY_WATER)
            }
            player.world?.setBlock(placeLocation, placedBlock, placedBlock.layer)
            if (placedBlock is BlockLiquid) {
                player.world?.scheduleBlockUpdate(placeLocation, placedBlock.tickRate.toLong())
            }
            if (player.gameMode != GameMode.CREATIVE) {
                if (amount - 1 <= 0) {
                    player.inventory.itemInHand = playerBucketEmptyEvent.itemInHand
                } else {
                    val clone = clone()
                    clone.setAmount(amount - 1)
                    player.inventory.itemInHand = clone
                    if (!player.inventory.addItem(playerBucketEmptyEvent.itemInHand)) {
                        player.world?.dropItem(create(ItemType.BUCKET), player.location, null)
                    }
                }
            }
        }
        return true
    }
}
