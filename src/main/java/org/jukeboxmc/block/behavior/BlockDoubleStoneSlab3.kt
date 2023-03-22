package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.data.StoneSlab3Type
import org.jukeboxmc.util.Identifier
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockDoubleStoneSlab3 : BlockSlab {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    fun setStoneSlabType(stoneSlabType: StoneSlab3Type): BlockDoubleStoneSlab3 {
        return setState<BlockDoubleStoneSlab3>("stone_slab_type_3", stoneSlabType.name.lowercase(Locale.getDefault()))
    }

    val stoneSlabType: StoneSlab3Type
        get() = if (stateExists("stone_slab_type_3")) StoneSlab3Type.valueOf(getStringState("stone_slab_type_3")) else StoneSlab3Type.END_STONE_BRICK
}
