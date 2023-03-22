package org.jukeboxmc.block.direction

/**
 * @author LucGamesYT
 * @version 1.0
 */
enum class TorchFacing {
    UNKNOWN, WEST, EAST, NORTH, SOUTH, TOP;

    fun toBlockFace(): BlockFace? {
        return when (this) {
            WEST -> BlockFace.WEST
            EAST -> BlockFace.EAST
            NORTH -> BlockFace.NORTH
            SOUTH -> BlockFace.SOUTH
            TOP -> BlockFace.UP
            else -> null
        }
    }
}
