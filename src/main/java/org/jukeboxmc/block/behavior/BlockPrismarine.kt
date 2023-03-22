package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.PrismarineType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemPrismarine
import org.jukeboxmc.util.Identifier
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockPrismarine : Block {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun toItem(): Item {
        return Item.create<ItemPrismarine>(ItemType.PRISMARINE).setPrismarineType(
            prismarineType,
        )
    }

    fun setPrismarineType(prismarineType: PrismarineType): BlockPrismarine {
        return setState("prismarine_block_type", prismarineType.name.lowercase(Locale.getDefault()))
    }

    val prismarineType: PrismarineType
        get() = if (stateExists("prismarine_block_type")) PrismarineType.valueOf(getStringState("prismarine_block_type")) else PrismarineType.DEFAULT
}
