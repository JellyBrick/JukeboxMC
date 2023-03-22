package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.StoneType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemStone
import org.jukeboxmc.util.Identifier
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockStone : Block {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun toItem(): Item {
        return Item.create<ItemStone>(ItemType.STONE).setStoneType(
            stoneType,
        )
    }

    override fun getDrops(item: Item?): List<Item> {
        return if (item != null && isCorrectToolType(item) && isCorrectTierType(item)) {
            listOf(
                Item.create<Item>(
                    ItemType.COBBLESTONE,
                ),
            )
        } else {
            emptyList()
        }
    }

    fun setStoneType(stoneType: StoneType): BlockStone {
        return setState<BlockStone>("stone_type", stoneType.name.lowercase(Locale.getDefault()))
    }

    val stoneType: StoneType
        get() = if (stateExists("stone_type")) StoneType.valueOf(getStringState("stone_type")) else StoneType.STONE
}
