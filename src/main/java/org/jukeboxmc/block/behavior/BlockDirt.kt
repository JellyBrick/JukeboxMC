package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.DirtType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemDirt
import org.jukeboxmc.util.Identifier
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockDirt : Block {
    constructor(identifier: Identifier?) : super(identifier)
    constructor(identifier: Identifier?, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun toItem(): Item {
        return Item.create<ItemDirt>(ItemType.DIRT).setDirtType(dirtType)
    }

    fun setDirtType(dirtType: DirtType): BlockDirt {
        return setState("dirt_type", dirtType.name.lowercase(Locale.getDefault()))
    }

    val dirtType: DirtType
        get() = if (stateExists("dirt_type")) DirtType.valueOf(getStringState("dirt_type")) else DirtType.NORMAL
}
