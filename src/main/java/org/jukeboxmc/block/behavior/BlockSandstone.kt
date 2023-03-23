package org.jukeboxmc.block.behavior

import org.cloudburstmc.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.SandStoneType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemSandstone
import org.jukeboxmc.util.Identifier
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockSandstone : Block {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun toItem(): Item {
        return Item.create<ItemSandstone>(ItemType.SANDSTONE).setSandStoneType(
            sandStoneType,
        )
    }

    fun setSandStoneType(sandStoneType: SandStoneType): BlockSandstone {
        setState<Block>("sand_stone_type", sandStoneType.name.lowercase(Locale.getDefault()))
        return this
    }

    val sandStoneType: SandStoneType
        get() = if (stateExists("sand_stone_type")) SandStoneType.valueOf(getStringState("sand_stone_type")) else SandStoneType.DEFAULT
}
