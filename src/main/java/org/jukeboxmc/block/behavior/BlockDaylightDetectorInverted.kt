package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockDaylightDetectorInverted : Block {
    constructor(identifier: Identifier?) : super(identifier)
    constructor(identifier: Identifier?, blockStates: NbtMap?) : super(identifier, blockStates)

    var redstoneSignal: Int
        get() = if (stateExists("redstone_signal")) getIntState("redstone_signal") else 0
        set(value) {
            setState<Block>("redstone_signal", value)
        }
}