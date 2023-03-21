package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import java.util.Locale
import org.jukeboxmc.block.data.StoneSlabType
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockDoubleStoneSlab : BlockSlab {
    constructor(identifier: Identifier?) : super(identifier)
    constructor(identifier: Identifier?, blockStates: NbtMap?) : super(identifier, blockStates)

    fun setStoneSlabType(stoneSlabType: StoneSlabType): BlockDoubleStoneSlab {
        return setState("stone_slab_type", stoneSlabType.name.lowercase(Locale.getDefault()))
    }

    val stoneSlabType: StoneSlabType
        get() = if (stateExists("stone_slab_type")) StoneSlabType.valueOf(getStringState("stone_slab_type")) else StoneSlabType.SMOOTH_STONE
}