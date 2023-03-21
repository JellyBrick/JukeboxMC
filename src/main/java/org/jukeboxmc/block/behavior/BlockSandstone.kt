package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import java.util.Locale
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.SandStoneType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemSandstone
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockSandstone : Block {
    constructor(identifier: Identifier?) : super(identifier)
    constructor(identifier: Identifier?, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun toItem(): Item {
        return Item.Companion.create<ItemSandstone>(ItemType.SANDSTONE).setSandStoneType(
            sandStoneType
        )
    }

    fun setSandStoneType(sandStoneType: SandStoneType): BlockSandstone {
        setState<Block>("sand_stone_type", sandStoneType.name.lowercase(Locale.getDefault()))
        return this
    }

    val sandStoneType: SandStoneType
        get() = if (stateExists("sand_stone_type")) SandStoneType.valueOf(getStringState("sand_stone_type")) else SandStoneType.DEFAULT
}