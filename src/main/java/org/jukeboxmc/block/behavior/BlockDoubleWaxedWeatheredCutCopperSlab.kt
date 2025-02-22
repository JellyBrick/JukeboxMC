package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockDoubleWaxedWeatheredCutCopperSlab : BlockSlab {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)
}
