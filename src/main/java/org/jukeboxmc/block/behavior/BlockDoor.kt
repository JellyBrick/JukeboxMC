package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import com.nukkitx.protocol.bedrock.data.LevelEventType
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.block.direction.Direction
import org.jukeboxmc.item.Item
import org.jukeboxmc.math.Location
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockDoor : Block {
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
        val block = world.getBlock(placePosition)
        direction = Direction.Companion.fromAngle(player.yaw)
        val blockAbove: BlockDoor = Block.Companion.create<BlockDoor>(this.type)
        blockAbove.setLocation(Location(world, placePosition.add(0f, 1f, 0f)))
        blockAbove.direction = direction
        blockAbove.setUpperBlock(true)
        blockAbove.setOpen(false)
        val blockLeft = world.getBlock(placePosition).getSide(player.direction.leftDirection)
        if (blockLeft.identifier == identifier) {
            blockAbove.setDoorHinge(true)
            setDoorHinge(true)
        } else {
            blockAbove.setDoorHinge(false)
            setDoorHinge(false)
        }
        setUpperBlock(false)
        setOpen(false)
        world.setBlock(placePosition.add(0f, 1f, 0f), blockAbove, 0)
        world.setBlock(placePosition, this, 0)

        /*TODO WATER LOGGING
        if ( block.isWater() ) {
            world.setBlock( placePosition.add( 0, 1, 0 ), block, 1 );
            world.setBlock( placePosition, block, 1 );
        }
         */return true
    }

    override fun interact(
        player: Player,
        blockPosition: Vector,
        clickedPosition: Vector?,
        blockFace: BlockFace?,
        itemInHand: Item
    ): Boolean {
        setOpen(!isOpen)
        location.world.sendLevelEvent(location, LevelEventType.SOUND_DOOR_OPEN, 0)
        return true
    }

    override fun onBlockBreak(breakPosition: Vector) {
        val block = location.world.getBlock(breakPosition, 1)
        if (isUpperBlock) {
            location.world.setBlock(location!!.subtract(0f, 1f, 0f), block, 0)
            location.world.setBlock(location!!.subtract(0f, 1f, 0f), Block.Companion.create<Block>(BlockType.AIR), 1)
        } else {
            location.world.setBlock(location!!.add(0f, 1f, 0f), block, 0)
            location.world.setBlock(location!!.add(0f, 1f, 0f), Block.Companion.create<Block>(BlockType.AIR), 1)
        }
        location.world.setBlock(location, block, 0)
        location.world.setBlock(location, Block.Companion.create<Block>(BlockType.AIR), 1)
    }

    fun setOpen(value: Boolean): BlockDoor {
        return setState("open_bit", if (value) 1.toByte() else 0.toByte())
    }

    val isOpen: Boolean
        get() = stateExists("open_bit") && getByteState("open_bit").toInt() == 1

    fun setUpperBlock(value: Boolean): BlockDoor {
        return setState("upper_block_bit", if (value) 1.toByte() else 0.toByte())
    }

    val isUpperBlock: Boolean
        get() = stateExists("upper_block_bit") && getByteState("upper_block_bit").toInt() == 1

    fun setDoorHinge(value: Boolean): BlockDoor {
        return setState("door_hinge_bit", if (value) 1.toByte() else 0.toByte())
    }

    val isDoorHinge: Boolean
        get() = stateExists("door_hinge_bit") && getByteState("door_hinge_bit").toInt() == 1
    var direction: Direction
        get() {
            val value = if (stateExists("direction")) getIntState("direction") else 0
            return when (value) {
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