package org.jukeboxmc.block.behavior

import org.cloudburstmc.nbt.NbtMap
import org.jukeboxmc.block.data.StoneSlabType
import org.jukeboxmc.util.Identifier
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockDoubleStoneSlab : BlockSlab {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    fun setStoneSlabType(stoneSlabType: StoneSlabType): BlockDoubleStoneSlab {
        return setState("stone_slab_type", stoneSlabType.name.lowercase(Locale.getDefault()))
    }

    val stoneSlabType: StoneSlabType
        get() = if (stateExists("stone_slab_type")) StoneSlabType.valueOf(getStringState("stone_slab_type")) else StoneSlabType.SMOOTH_STONE
}
