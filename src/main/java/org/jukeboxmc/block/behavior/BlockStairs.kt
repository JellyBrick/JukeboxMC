package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.direction.BlockFace
import org.jukeboxmc.block.direction.CrossDirection
import org.jukeboxmc.item.Item
import org.jukeboxmc.math.Vector
import org.jukeboxmc.player.Player
import org.jukeboxmc.util.Identifier
import org.jukeboxmc.world.World

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockStairs : Block {
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
        crossDirection = player.direction.toCrossDirection()
        if (clickedPosition.y > 0.5 && blockFace != BlockFace.UP || blockFace == BlockFace.DOWN) {
            isUpsideDown = true
        }
        return super.placeBlock(player, world, blockPosition, placePosition, clickedPosition, itemInHand, blockFace)
    }

    var isUpsideDown: Boolean
        get() = stateExists("upside_down_bit") && getByteState("upside_down_bit").toInt() == 1
        set(value) {
            setState<Block>("upside_down_bit", if (value) 1.toByte() else 0.toByte())
        }
    var crossDirection: CrossDirection
        get() {
            val value = if (stateExists("weirdo_direction")) getIntState("weirdo_direction") else 0
            return when (value) {
                0 -> CrossDirection.EAST
                1 -> CrossDirection.WEST
                2 -> CrossDirection.SOUTH
                else -> CrossDirection.NORTH
            }
        }
        set(crossDirection) {
            when (crossDirection) {
                CrossDirection.EAST -> setState<Block>("weirdo_direction", 0)
                CrossDirection.WEST -> setState<Block>("weirdo_direction", 1)
                CrossDirection.SOUTH -> setState<Block>("weirdo_direction", 2)
                else -> setState<Block>("weirdo_direction", 3)
            }
        }
}