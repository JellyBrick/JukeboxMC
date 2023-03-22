package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.data.StoneSlab4Type
import org.jukeboxmc.util.Identifier
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockDoubleStoneSlab4 : BlockSlab {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    fun setStoneSlabType(stoneSlabType: StoneSlab4Type): BlockDoubleStoneSlab4 {
        return setState("stone_slab_type_4", stoneSlabType.name.lowercase(Locale.getDefault()))
    }

    val stoneSlabType: StoneSlab4Type
        get() = if (stateExists("stone_slab_type_4")) StoneSlab4Type.valueOf(getStringState("stone_slab_type_4")) else StoneSlab4Type.STONE
}
