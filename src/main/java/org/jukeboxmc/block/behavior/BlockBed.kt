package org.jukeboxmc.block.behavior

import org.cloudburstmc.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.data.BlockColor
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.block.direction.Direction
import org.jukeboxmc.blockentity.BlockEntity
import org.jukeboxmc.blockentity.BlockEntityBed
import org.jukeboxmc.blockentity.BlockEntityType
import org.jukeboxmc.item.Item
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockBed : Block {
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
        val blockColor = BlockColor.values()[itemInHand.meta]
        val blockDirection = this.getSide(player.direction)
        val directionLocation = blockDirection.location
        if (blockDirection.canBeReplaced(this) && blockDirection.isTransparent) {
            direction = player.direction
            val blockBed: BlockBed = create(BlockType.BED)
            blockBed.location = directionLocation
            blockBed.direction = direction
            blockBed.isHeadPiece = true
            world.setBlock(directionLocation, blockBed, 0)
            world.setBlock(placePosition, this, 0)
            BlockEntity.create<BlockEntityBed>(BlockEntityType.BED, blockBed).setColor(blockColor).spawn()
            BlockEntity.create<BlockEntityBed>(BlockEntityType.BED, this).setColor(blockColor).spawn()
            return true
        }
        return false
    }

    override fun onBlockBreak(breakPosition: Vector) {
        var direction = direction
        if (isHeadPiece) {
            direction = direction.opposite()
        }
        val otherBlock = this.getSide(direction)
        if (otherBlock is BlockBed) {
            if (otherBlock.isHeadPiece != isHeadPiece) {
                location.world.setBlock(otherBlock.location, create(BlockType.AIR), 0)
            }
        }
        location.world.setBlock(breakPosition, create(BlockType.AIR), 0)
    }

    override val blockEntity: BlockEntity?
        get() = location.world.getBlockEntity(location, location.dimension) as BlockEntityBed?
    var isHeadPiece: Boolean
        get() = stateExists("head_piece_bit") && getByteState("head_piece_bit").toInt() == 1
        set(value) {
            setState<Block>("head_piece_bit", if (value) 1.toByte() else 0.toByte())
        }
    var isOccupied: Boolean
        get() = stateExists("occupied_bit") && getByteState("occupied_bit").toInt() == 1
        set(value) {
            setState<Block>("occupied_bit", if (value) 1.toByte() else 0.toByte())
        }
    var direction: Direction
        get() {
            return when (if (stateExists("direction")) getIntState("direction") else 0) {
                0 -> Direction.SOUTH
                1 -> Direction.WEST
                2 -> Direction.NORTH
                else -> Direction.EAST
            }
        }
        set(direction) {
            when (direction) {
                Direction.SOUTH -> setState<Block>("direction", 0)
                Direction.WEST -> setState<Block>("direction", 1)
                Direction.NORTH -> setState<Block>("direction", 2)
                Direction.EAST -> setState<Block>("direction", 3)
            }
        }
}
