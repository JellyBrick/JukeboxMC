package org.jukeboxmc.network.handler

import com.nukkitx.protocol.bedrock.data.SoundEvent
import com.nukkitx.protocol.bedrock.data.inventory.TransactionType
import com.nukkitx.protocol.bedrock.packet.InventoryTransactionPacket
import org.jukeboxmc.Server
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockSlab
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.event.player.PlayerInteractEvent
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemAir
import org.jukeboxmc.item.behavior.ItemBow
import org.jukeboxmc.math.Location
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.GameMode
import org.jukeboxmc.player.Player

/**
 * @author LucGamesYT
 * @version 1.0
 */
class InventoryTransactionHandler : PacketHandler<InventoryTransactionPacket> {
    private var spamCheckTime: Long = 0
    override fun handle(packet: InventoryTransactionPacket, server: Server, player: Player) {
        if (packet.getTransactionType() == TransactionType.ITEM_USE) {
            val blockPosition: Vector = Vector(packet.blockPosition)
            blockPosition.dimension = player.dimension
            val clickPosition: Vector = Vector(packet.clickPosition)
            clickPosition.dimension = player.dimension
            val blockFace: BlockFace = BlockFace.fromId(packet.getBlockFace()) ?: run {
                Server.instance.logger.debug("Unknown block face: " + packet.getBlockFace())
                return
            }
            val itemInHand = player.inventory.itemInHand
            when (packet.getActionType()) {
                0 -> {
                    if (!canInteract()) {
                        player.world!!.getBlock(player.world!!.getSidePosition(blockPosition, blockFace)).sendUpdate(player)
                        return
                    }
                    spamCheckTime = System.currentTimeMillis()
                    val placePosition = player.world.getSidePosition(blockPosition, blockFace)
                    placePosition.dimension = player.dimension
                    player.setAction(false)
                    if (!useItemOn(player, blockPosition, placePosition, clickPosition, blockFace)) {
                        val blockClicked = player.world.getBlock(blockPosition)
                        blockClicked.sendUpdate(player)
                        val replacedBlock = player.world.getBlock(blockPosition).getSide(blockFace)
                        replacedBlock.sendUpdate(player)
                    }
                }

                1 -> {
                    val directionVector = player.location.direction
                    val playerInteractEvent = PlayerInteractEvent(
                        player,
                        PlayerInteractEvent.Action.RIGHT_CLICK_AIR,
                        itemInHand,
                        directionVector,
                    )
                    Server.instance.pluginManager.callEvent(playerInteractEvent)
                    if (itemInHand.useInAir(player, directionVector)) {
                        if (!player.hasAction()) {
                            player.setAction(true)
                            break
                        }
                        player.setAction(false)
                        if (!itemInHand.onUse(player)) {
                            player.inventory.sendContents(player)
                        }
                    }
                }

                2 -> {
                    val breakPosition: Vector = Vector(packet.getBlockPosition())
                    breakPosition.dimension = player.dimension
                    val block = player.world.getBlock(breakPosition)
                    if (block.type == BlockType.AIR) return
                    block.breakBlock(player, itemInHand)
                }
            }
        } else if (packet.getTransactionType() == TransactionType.NORMAL) {
            for (action in packet.getActions()) {
                if (action.getSource().getType() == InventorySource.Type.WORLD_INTERACTION) {
                    if (action.getSource().getFlag() == InventorySource.Flag.DROP_ITEM) {
                        val targetItem: Item = Item.create<Item>(action.getToItem())
                        val playerDropItemEvent = PlayerDropItemEvent(player, targetItem)
                        Server.instance.pluginManager.callEvent(playerDropItemEvent)
                        if (playerDropItemEvent.isCancelled()) {
                            player.inventory.sendContents(player)
                            return
                        }
                        val entityItem = player.world.dropItem(
                            playerDropItemEvent.getItem(),
                            player.location.add(0f, player.eyeHeight, 0f),
                            player.location.direction.multiply(0.4f, 0.4f, 0.4f),
                        )
                        entityItem.playerHasThrown = player
                        entityItem.spawn()
                    }
                } else if (action.getSource().getType() == InventorySource.Type.CONTAINER) {
                    val containerId: Int = action.getSource().getContainerId()
                    if (containerId == 0) {
                        val check = player.inventory.getItem(action.getSlot())
                        val sourceItem: Item = Item(action.getFromItem(), false)
                        val targetItem: Item = Item(action.getToItem(), false)
                        if (check.equalsExact(sourceItem)) {
                            check.removeFromHand(player)
                            player.inventory.setItem(action.getSlot(), targetItem, false)
                        } else {
                            player.inventory.sendContents(action.getSlot(), player)
                        }
                    }
                }
            }
        } else if (packet.getTransactionType() == TransactionType.ITEM_USE_ON_ENTITY) {
            when (packet.getActionType()) {
                0 -> {
                    val interactEntity = player.world.getEntity(packet.getRuntimeEntityId())
                    interactEntity?.interact(player, Vector(packet.getClickPosition()))
                }

                1 -> {
                    val entity = player.world.getEntity(packet.getRuntimeEntityId())
                    if (entity != null) {
                        if (player.attackWithItemInHand(entity)) {
                            if (player.gameMode != GameMode.CREATIVE) {
                                val itemInHand = player.inventory.itemInHand
                                itemInHand.updateItem(player, 1)
                            }
                        }
                    }
                }

                else -> {}
            }
        } else if (packet.getTransactionType() == TransactionType.ITEM_RELEASE) {
            if (packet.getActionType() == 0) {
                if (player.inventory.itemInHand is ItemBow) {
                    (player.inventory.itemInHand as ItemBow).shoot(player)
                }
            }
            player.setAction(false)
        } else {
            player.setAction(false)
        }
    }

    fun useItemOn(
        player: Player,
        blockPosition: Vector,
        placePosition: Vector,
        clickedPosition: Vector?,
        blockFace: BlockFace?,
    ): Boolean {
        var placePosition = placePosition
        val world = player.world
        val clickedBlock = world.getBlock(blockPosition)
        if (clickedBlock.type == BlockType.AIR) {
            return false
        }
        val itemInHand = player.inventory.itemInHand
        val replacedBlock = world.getBlock(placePosition)
        val placedBlock = itemInHand.toBlock()
        val location = Location(world, placePosition, player.dimension)
        placedBlock!!.location = location
        val playerInteractEvent = PlayerInteractEvent(
            player,
            if (clickedBlock.type == BlockType.AIR) PlayerInteractEvent.Action.RIGHT_CLICK_AIR else PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK,
            player.inventory.itemInHand,
            clickedBlock!!,
        )
        Server.instance.pluginManager.callEvent(playerInteractEvent)
        var interact = false
        if (!player.isSneaking) {
            if (!playerInteractEvent.isCancelled) {
                interact = clickedBlock!!.interact(player, blockPosition, clickedPosition, blockFace, itemInHand)
            }
        }
        val itemInteract = itemInHand.interact(player, blockFace, clickedPosition, clickedBlock)
        if (!interact && itemInHand.useOnBlock(player, clickedBlock, location)) {
            return true
        }
        if (itemInHand is ItemAir || itemInHand.toBlock().type == BlockType.AIR) {
            return interact
        }
        if (!interact && !itemInteract || player.isSneaking) {
            if (clickedBlock!!.canBeReplaced(placedBlock)) {
                placePosition = blockPosition
            } else if (!(replacedBlock!!.canBeReplaced(placedBlock) || player.inventory.itemInHand.toBlock() is BlockSlab && replacedBlock is BlockSlab)) {
                return false
            }
            if (placedBlock!!.isSolid) {
                val boundingBox = player.boundingBox
                if (placedBlock.boundingBox.intersectsWith(boundingBox)) {
                    return false
                }
            }
            if (player.gameMode == GameMode.SPECTATOR) {
                return false
            }
            val blockPlaceEvent = BlockPlaceEvent(player, placedBlock, replacedBlock, clickedBlock)
            Server.instance.pluginManager.callEvent(blockPlaceEvent)
            if (blockPlaceEvent.isCancelled()) {
                return false
            }
            val success: Boolean = blockPlaceEvent.getPlacedBlock()
                .placeBlock(player, world, blockPosition, placePosition, clickedPosition, itemInHand, blockFace)
            if (success) {
                if (player.gameMode == GameMode.SURVIVAL) {
                    val resultItem = itemInHand.setAmount(itemInHand.amount - 1)
                    if (itemInHand.amount != 0) {
                        player.inventory.itemInHand = resultItem
                    } else {
                        player.inventory.itemInHand =
                            Item.create<Item>(ItemType.AIR)
                    }
                    player.inventory.sendContents(player.inventory.itemInHandSlot, player)
                }
                world.playSound(placePosition, SoundEvent.PLACE, placedBlock.runtimeId)
            }
            return success
        }
        return true
    }

    fun canInteract(): Boolean {
        return System.currentTimeMillis() - spamCheckTime >= 100
    }
}
