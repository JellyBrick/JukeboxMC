package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
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
class BlockEndPortalFrame : Block {
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
        direction = player.direction.opposite()
        return super.placeBlock(player, world, blockPosition, placePosition, clickedPosition, itemInHand, blockFace)
    }

    var isEndPortalEye: Boolean
        get() = stateExists("end_portal_eye_bit") && getByteState("end_portal_eye_bit").toInt() == 1
        set(value) {
            setState<Block>("end_portal_eye_bit", if (value) 1.toByte() else 0.toByte())
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
