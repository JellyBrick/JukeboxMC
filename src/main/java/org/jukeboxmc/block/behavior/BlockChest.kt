package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.block.direction.Direction
import org.jukeboxmc.blockentity.BlockEntity
import org.jukeboxmc.blockentity.BlockEntityChest
import org.jukeboxmc.blockentity.BlockEntityType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockChest : Block {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun placeBlock(
        player: Player,
        world: World,
        blockPosition: Vector,
        placePosition: Vector,
        clickedPosition: Vector,
        itemInHand: Item,
        blockFace: BlockFace,
    ): Boolean {
        this.blockFace = player.direction.toBlockFace().opposite()
        val value =
            super.placeBlock(player, world, blockPosition, placePosition, clickedPosition, itemInHand, blockFace)
        if (value) {
            BlockEntity.create<BlockEntity>(BlockEntityType.CHEST, this).spawn()
            for (direction in Direction.values()) {
                val side = this.getSide(direction)
                if (side.type == this.type) {
                    val blockEntity: BlockEntityChest? = blockEntity as BlockEntityChest?
                    val sideBlockEntity = side.blockEntity as BlockEntityChest?
                    if (blockEntity != null && sideBlockEntity != null && !blockEntity.isPaired && !sideBlockEntity.isPaired) {
                        blockEntity.pair(sideBlockEntity)
                        blockEntity.update(player)
                        sideBlockEntity.update(player)
                    }
                }
            }
        }
        return value
    }

    override fun interact(
        player: Player,
        blockPosition: Vector,
        clickedPosition: Vector?,
        blockFace: BlockFace?,
        itemInHand: Item,
    ): Boolean {
        val blockEntity: BlockEntityChest? = blockEntity as BlockEntityChest?
        if (blockEntity != null) {
            blockEntity.interact(player, blockPosition, clickedPosition, blockFace, itemInHand)
            return true
        }
        return false
    }

    override fun onBlockBreak(breakPosition: Vector) {
        val blockEntity: BlockEntityChest? = blockEntity as BlockEntityChest?
        if (blockEntity != null) {
            if (blockEntity.isPaired) {
                blockEntity.unpair()
            }
            val chestInventory = blockEntity.getChestInventory()
            for (content in chestInventory.contents) {
                if (content.type != ItemType.AIR) {
                    location.world?.dropItem(content, breakPosition, null)?.spawn()
                }
            }
            chestInventory.clear()
            chestInventory.viewer.clear()
        }
        super.onBlockBreak(breakPosition)
    }

    override val blockEntity: BlockEntity?
        get() = location.world?.getBlockEntity(location, location.dimension) as BlockEntityChest?
    var blockFace: BlockFace
        get() = if (stateExists("facing_direction")) BlockFace.values()[getIntState("facing_direction")] else BlockFace.NORTH
        set(blockFace) {
            setState<Block>("facing_direction", blockFace.ordinal)
        }
}
