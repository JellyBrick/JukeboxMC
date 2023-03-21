package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.direction.BlockFace
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
class BlockSugarCane : Block {
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
        val block = world.getBlock(blockPosition)
        if (world.getBlock(placePosition.subtract(0f, 1f, 0f)).type == BlockType.SUGAR_CANE) {
            world.setBlock(blockPosition.add(0f, 1f, 0f), this)
            return true
        } else if (block.type == BlockType.GRASS || block.type == BlockType.DIRT || block.type == BlockType.SAND) {
            val blockNorth = block!!.getSide(BlockFace.NORTH)
            val blockEast = block.getSide(BlockFace.EAST)
            val blockSouth = block.getSide(BlockFace.SOUTH)
            val blockWest = block.getSide(BlockFace.WEST)
            if (blockNorth.type == BlockType.WATER || blockEast.type == BlockType.WATER || blockSouth.type == BlockType.WATER || blockWest.type == BlockType.WATER) {
                world.setBlock(placePosition, this)
                return true
            }
        }
        return false
    }

    override fun toItem(): Item {
        return Item.Companion.create<Item>(ItemType.SUGAR_CANE)
    }

    var age: Int
        get() = if (stateExists("age")) getIntState("age") else 0
        set(value) {
            setState<Block>("age", value)
        }
}