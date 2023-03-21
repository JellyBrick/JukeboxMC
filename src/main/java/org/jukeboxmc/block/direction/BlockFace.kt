package org.jukeboxmc.block.direction

import org.jukeboxmc.math.Vector

/**
 * @author LucGamesYT
 * @version 1.0
 */
enum class BlockFace(val horizontalIndex: Int, val offset: Vector) {
    DOWN(-1, Vector(0, -1, 0)), UP(-1, Vector(0, 1, 0)), NORTH(2, Vector(0, 0, -1)), SOUTH(0, Vector(0, 0, 1)), WEST(
        1,
        Vector(-1, 0, 0)
    ),
    EAST(3, Vector(1, 0, 0));

    fun opposite(): BlockFace {
        return when (this) {
            DOWN -> UP
            UP -> DOWN
            NORTH -> SOUTH
            WEST -> EAST
            EAST -> WEST
            else -> NORTH
        }
    }

    fun toDirection(): Direction? {
        return when (this) {
            NORTH -> Direction.NORTH
            EAST -> Direction.EAST
            SOUTH -> Direction.SOUTH
            WEST -> Direction.WEST
            else -> null
        }
    }

    fun torchFacing(): TorchFacing {
        return when (this) {
            UP -> TorchFacing.TOP
            WEST -> TorchFacing.WEST
            EAST -> TorchFacing.EAST
            NORTH -> TorchFacing.NORTH
            SOUTH -> TorchFacing.SOUTH
            else -> TorchFacing.UNKNOWN
        }
    }

    companion object {
        fun fromId(value: Int): BlockFace? {
            return when (value) {
                0 -> DOWN
                1 -> UP
                2 -> NORTH
                3 -> SOUTH
                4 -> WEST
                5 -> EAST
                else -> null
            }
        }

        val horizontal: Array<BlockFace>
            get() = arrayOf(NORTH, EAST, SOUTH, WEST)
    }
}