package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import java.util.Locale
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.WoodType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemPlanks
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockPlanks : Block {
    constructor(identifier: Identifier?) : super(identifier)
    constructor(identifier: Identifier?, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun toItem(): Item {
        return Item.Companion.create<ItemPlanks>(ItemType.PLANKS).setWoodType(
            woodType
        )
    }

    fun setWoodType(woodType: WoodType): Block {
        return setState("wood_type", woodType.name.lowercase(Locale.getDefault()))
    }

    val woodType: WoodType
        get() = if (stateExists("wood_type")) WoodType.valueOf(getStringState("wood_type")) else WoodType.OAK
}