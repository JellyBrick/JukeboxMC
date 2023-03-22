package org.jukeboxmc.block.direction

/**
 * @author LucGamesYT
 * @version 1.0
 */
enum class Direction {
    NORTH, EAST, SOUTH, WEST;

    fun opposite(): Direction {
        return when (this) {
            NORTH -> SOUTH
            WEST -> EAST
            EAST -> WEST
            else -> NORTH
        }
    }

    val rightDirection: Direction
        get() = when (this) {
            NORTH -> EAST
            EAST -> SOUTH
            SOUTH -> WEST
            else -> NORTH
        }
    val leftDirection: Direction
        get() = when (this) {
            NORTH -> WEST
            EAST -> NORTH
            SOUTH -> EAST
            else -> SOUTH
        }

    fun toBlockFace(): BlockFace {
        return when (this) {
            SOUTH -> BlockFace.SOUTH
            WEST -> BlockFace.WEST
            NORTH -> BlockFace.NORTH
            else -> BlockFace.EAST
        }
    }

    fun toCrossDirection(): CrossDirection {
        return when (this) {
            SOUTH -> CrossDirection.SOUTH
            WEST -> CrossDirection.WEST
            NORTH -> CrossDirection.NORTH
            else -> CrossDirection.EAST
        }
    }

    companion object {
        fun fromAngle(value: Float): Direction {
            var value = value
            value -= 90f
            value %= 360f
            if (value < 0) {
                value += 360.0.toFloat()
            }
            if (0 <= value && value < 45 || 315 <= value && value < 360) {
                return NORTH
            }
            if (45 <= value && value < 135) {
                return EAST
            }
            return if (135 <= value && value < 225) {
                SOUTH
            } else {
                WEST
            }
        }
    }
}
