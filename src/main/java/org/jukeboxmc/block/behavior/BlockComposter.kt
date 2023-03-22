package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockComposter : Block {
    constructor(identifier: Identifier?) : super(identifier)
    constructor(identifier: Identifier?, blockStates: NbtMap?) : super(identifier, blockStates)

    var composterFillLevel: Int
        get() = if (stateExists("composter_fill_level")) getIntState("composter_fill_level") else 0
        set(value) {
            setState<Block>("composter_fill_level", value)
        }
}
