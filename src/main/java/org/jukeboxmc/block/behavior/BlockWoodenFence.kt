package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.WoodType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemFence
import org.jukeboxmc.util.Identifier
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockWoodenFence : BlockFence {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun toItem(): Item {
        return Item.create<ItemFence>(ItemType.FENCE).setWoodType(
            woodType,
        )
    }

    fun setWoodType(woodType: WoodType): BlockFence {
        setState<Block>("wood_type", woodType.name.lowercase(Locale.getDefault()))
        return this
    }

    val woodType: WoodType
        get() = if (stateExists("wood_type")) WoodType.valueOf(getStringState("wood_type")) else WoodType.OAK
}
