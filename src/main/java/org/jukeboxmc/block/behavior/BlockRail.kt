package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.direction.RailDirection
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockRail : Block {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    fun setRailDirection(railDirection: RailDirection): BlockRail {
        setState<Block>("rail_direction", railDirection.ordinal)
        return this
    }

    val railDirection: RailDirection
        get() = if (stateExists("rail_direction")) RailDirection.values()[getIntState("rail_direction")] else RailDirection.NORTH_SOUTH
}
