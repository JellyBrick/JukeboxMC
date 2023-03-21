package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.apache.commons.math3.util.FastMath
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.blockentity.BlockEntity
import org.jukeboxmc.blockentity.BlockEntityBarrel
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
class BlockBarrel : Block {
    constructor(identifier: Identifier?) : super(identifier)
    constructor(identifier: Identifier?, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun placeBlock(
        player: Player,
        world: World,
        blockPosition: Vector,
        placePosition: Vector,
        clickedPosition: Vector,
        itemInHand: Item,
        blockFace: BlockFace
    ): Boolean {
        if (FastMath.abs(
                player.x - getLocation()!!
                    .x
            ) < 2 && FastMath.abs(player.z - getLocation()!!.z) < 2
        ) {
            val y = (player.y + player.eyeHeight).toDouble()
            if (y - getLocation()!!.y > 2) {
                this.blockFace = BlockFace.UP
            } else if (getLocation()!!.y - y > 0) {
                this.blockFace = BlockFace.DOWN
            } else {
                this.blockFace = player.direction.toBlockFace().opposite()
            }
        } else {
            this.blockFace = player.direction.toBlockFace().opposite()
        }
        isOpen = false
        world.setBlock(placePosition, this, 0)
        BlockEntity.Companion.create<BlockEntity>(BlockEntityType.BARREL, this).spawn()
        return true
    }

    override fun interact(
        player: Player,
        blockPosition: Vector,
        clickedPosition: Vector?,
        blockFace: BlockFace?,
        itemInHand: Item
    ): Boolean {
        val blockEntity: BlockEntityBarrel? = blockEntity
        if (blockEntity != null) {
            blockEntity.interact(player, blockPosition, clickedPosition, blockFace, itemInHand)
            return true
        }
        return false
    }

    override fun onBlockBreak(breakPosition: Vector) {
        val blockEntity: BlockEntityBarrel? = blockEntity
        if (blockEntity != null) {
            val inventory = blockEntity.barrelInventory
            for (content in inventory.contents) {
                if (content != null && content.type != ItemType.AIR) {
                    location.world.dropItem(content, breakPosition, null).spawn()
                }
            }
            inventory.clear()
            inventory.viewer.clear()
        }
        super.onBlockBreak(breakPosition)
    }

    override val blockEntity: BlockEntity?
        get() = location.world.getBlockEntity(location, location.dimension) as BlockEntityBarrel?
    var isOpen: Boolean
        get() = stateExists("open_bit") && getByteState("open_bit").toInt() == 1
        set(value) {
            setState<Block>("open_bit", if (value) 1.toByte() else 0.toByte())
        }
    var blockFace: BlockFace
        get() = if (stateExists("facing_direction")) BlockFace.values()[getIntState("facing_direction")] else BlockFace.NORTH
        set(blockFace) {
            setState<Block>("facing_direction", blockFace.ordinal)
        }
}