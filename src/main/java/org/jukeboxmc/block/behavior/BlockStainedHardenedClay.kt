package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.BlockColor
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemStainedHardenedClay
import org.jukeboxmc.util.Identifier
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockStainedHardenedClay : Block {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun toItem(): Item {
        return Item.create<ItemStainedHardenedClay>(ItemType.STAINED_HARDENED_CLAY).setColor(
            color,
        )
    }

    fun setColor(color: BlockColor): BlockStainedHardenedClay {
        return setState("color", color.name.lowercase(Locale.getDefault()))
    }

    val color: BlockColor
        get() = if (stateExists("color")) BlockColor.valueOf(getStringState("color")) else BlockColor.WHITE
}
