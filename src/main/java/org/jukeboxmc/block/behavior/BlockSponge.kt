package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.SpongeType
import org.jukeboxmc.util.Identifier
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockSponge : Block {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    fun setSpongeType(spongeType: SpongeType): BlockSponge {
        return setState("sponge_type", spongeType.name.lowercase(Locale.getDefault()))
    }

    val spongeType: SpongeType
        get() = if (stateExists("sponge_type")) SpongeType.valueOf(getStringState("sponge_type")) else SpongeType.DRY
}
