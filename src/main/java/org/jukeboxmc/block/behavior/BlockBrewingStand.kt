package org.jukeboxmc.block.behavior

import org.cloudburstmc.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.blockentity.BlockEntity
import org.jukeboxmc.blockentity.BlockEntityBrewingStand
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
class BlockBrewingStand : Block {
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
        val value =
            super.placeBlock(player, world, blockPosition, placePosition, clickedPosition, itemInHand, blockFace)
        if (value) {
            BlockEntity.create<BlockEntity>(BlockEntityType.BREWING_STAND, this).spawn()
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
        val blockEntity: BlockEntityBrewingStand? = blockEntity as BlockEntityBrewingStand?
        if (blockEntity != null) {
            blockEntity.interact(player, blockPosition, clickedPosition, blockFace, itemInHand)
            return true
        }
        return false
    }

    override fun onBlockBreak(breakPosition: Vector) {
        val blockEntity: BlockEntityBrewingStand? = blockEntity as BlockEntityBrewingStand?
        if (blockEntity != null) {
            val inventory = blockEntity.brewingStandInventory
            for (content in inventory.contents) {
                if (content.type != ItemType.AIR) {
                    location.world.dropItem(content, breakPosition, null)?.spawn()
                }
            }
            inventory.clear()
            inventory.viewer.clear()
        }
        super.onBlockBreak(breakPosition)
    }

    override val blockEntity: BlockEntity?
        get() = location.world.getBlockEntity(location, location.dimension) as BlockEntityBrewingStand?
    var isBrewingStandSlotA: Boolean
        get() = stateExists("brewing_stand_slot_a_bit") && getByteState("brewing_stand_slot_a_bit").toInt() == 1
        set(value) {
            setState<Block>("brewing_stand_slot_a_bit", if (value) 1.toByte() else 0.toByte())
        }
    var isBrewingStandSlotB: Boolean
        get() = stateExists("brewing_stand_slot_b_bit") && getByteState("brewing_stand_slot_b_bit").toInt() == 1
        set(value) {
            setState<Block>("brewing_stand_slot_a_bit", if (value) 1.toByte() else 0.toByte())
        }
    var isBrewingStandSlotC: Boolean
        get() = stateExists("brewing_stand_slot_c_bit") && getByteState("brewing_stand_slot_c_bit").toInt() == 1
        set(value) {
            setState<Block>("brewing_stand_slot_a_bit", if (value) 1.toByte() else 0.toByte())
        }
}
