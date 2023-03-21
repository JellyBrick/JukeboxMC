package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import java.util.Locale
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.StoneBrickType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemStonebrick
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockStonebrick : Block {
    constructor(identifier: Identifier?) : super(identifier)
    constructor(identifier: Identifier?, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun toItem(): Item {
        return Item.Companion.create<ItemStonebrick>(ItemType.STONEBRICK).setStoneBrickType(
            stoneBrickType
        )
    }

    fun setStoneBrickType(stoneBrickType: StoneBrickType): BlockStonebrick {
        return setState("stone_brick_type", stoneBrickType.name.lowercase(Locale.getDefault()))
    }

    val stoneBrickType: StoneBrickType
        get() = if (stateExists("stone_brick_type")) StoneBrickType.valueOf(getStringState("stone_brick_type")) else StoneBrickType.DEFAULT
}