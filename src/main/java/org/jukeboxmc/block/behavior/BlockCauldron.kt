package org.jukeboxmc.block.behavior

import org.cloudburstmc.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.LiquidType
import org.jukeboxmc.util.Identifier
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockCauldron : Block {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    var fillLevel: Int
        get() = if (stateExists("fill_level")) getIntState("fill_level") else 0
        set(value) { // 0-6
            setState<Block>("fill_level", value)
        }

    fun setLiquid(liquidType: LiquidType) {
        setState<Block>("cauldron_liquid", liquidType.name.lowercase(Locale.getDefault()))
    }

    val liquidType: LiquidType
        get() = if (stateExists("cauldron_liquid")) LiquidType.valueOf(getStringState("cauldron_liquid")) else LiquidType.WATER
}
