package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import com.nukkitx.protocol.bedrock.data.LevelEventType
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.block.direction.Direction
import org.jukeboxmc.item.Item
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockTrapdoor : Block {
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
        val playerDirection = player.direction
        if (clickedPosition.y > 0.5 && blockFace != BlockFace.UP || blockFace == BlockFace.DOWN) {
            isUpsideDown = true
        }
        direction = when (playerDirection) {
            Direction.NORTH -> Direction.NORTH
            Direction.EAST -> Direction.WEST
            Direction.SOUTH -> Direction.EAST
            Direction.WEST -> Direction.SOUTH
        }
        isOpen = false
        return super.placeBlock(player, world, blockPosition, placePosition, clickedPosition, itemInHand, blockFace)
    }

    override fun interact(
        player: Player,
        blockPosition: Vector,
        clickedPosition: Vector?,
        blockFace: BlockFace?,
        itemInHand: Item,
    ): Boolean {
        isOpen = !isOpen
        location.world?.sendLevelEvent(location, LevelEventType.SOUND_DOOR_OPEN, 0)
        return true
    }

    var isOpen: Boolean
        get() = stateExists("open_bit") && getByteState("open_bit").toInt() == 1
        set(value) {
            setState<Block>("open_bit", if (value) 1.toByte() else 0.toByte())
        }
    var isUpsideDown: Boolean
        get() = stateExists("upside_down_bit") && getByteState("upside_down_bit").toInt() == 1
        set(value) {
            setState<Block>("upside_down_bit", if (value) 1.toByte() else 0.toByte())
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
