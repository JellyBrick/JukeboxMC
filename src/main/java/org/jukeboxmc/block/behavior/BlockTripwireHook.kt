package org.jukeboxmc.block.behavior

import org.cloudburstmc.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.direction.Direction
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockTripwireHook : Block {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    var isPowered: Boolean
        get() = stateExists("powered_bit") && getByteState("powered_bit").toInt() == 1
        set(value) {
            setState<Block>("powered_bit", if (value) 1.toByte() else 0.toByte())
        }
    var isAttached: Boolean
        get() = stateExists("attached_bit") && getByteState("attached_bit").toInt() == 1
        set(value) {
            setState<Block>("attached_bit", if (value) 1.toByte() else 0.toByte())
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
