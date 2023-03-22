package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.data.StoneSlab2Type
import org.jukeboxmc.util.Identifier
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockDoubleStoneSlab2 : BlockSlab {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    fun setStoneSlabType(stoneSlabType: StoneSlab2Type): BlockDoubleStoneSlab2 {
        return setState("stone_slab_type_2", stoneSlabType.name.lowercase(Locale.getDefault()))
    }

    val stoneSlabType: StoneSlab2Type
        get() = if (stateExists("stone_slab_type_2")) StoneSlab2Type.valueOf(getStringState("stone_slab_type_2")) else StoneSlab2Type.RED_SANDSTONE
}
