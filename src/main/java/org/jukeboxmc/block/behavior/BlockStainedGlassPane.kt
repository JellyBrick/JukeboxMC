package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import java.util.Locale
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.BlockColor
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemStainedGlassPane
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockStainedGlassPane : Block {
    constructor(identifier: Identifier?) : super(identifier)
    constructor(identifier: Identifier?, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun toItem(): Item {
        return Item.Companion.create<ItemStainedGlassPane>(ItemType.STAINED_GLASS_PANE).setColor(
            color
        )
    }

    fun setColor(color: BlockColor): BlockStainedGlassPane {
        setState<Block>("color", color.name.lowercase(Locale.getDefault()))
        return this
    }

    val color: BlockColor
        get() = if (stateExists("color")) BlockColor.valueOf(getStringState("color")) else BlockColor.WHITE
}