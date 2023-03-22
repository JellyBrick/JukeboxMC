package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.LeafType2
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemLeaves2
import org.jukeboxmc.util.Identifier
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockLeaves2 : Block {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun toItem(): Item {
        return Item.create<ItemLeaves2>(ItemType.LEAVES2).setLeafType(
            leafType,
        )
    }

    override fun getDrops(item: Item?): List<Item> {
        return if (item != null && isCorrectToolType(item)) {
            listOf(toItem())
        } else {
            emptyList()
        }
    }

    var isPersistent: Boolean
        get() = stateExists("persistent_bit") && getByteState("persistent_bit").toInt() == 1
        set(value) {
            setState<Block>("persistent_bit", if (value) 1.toByte() else 0.toByte())
        }
    var isUpdate: Boolean
        get() = stateExists("update_bit") && getByteState("update_bit").toInt() == 1
        set(value) {
            setState<Block>("update_bit", if (value) 1.toByte() else 0.toByte())
        }

    fun setLeafType(leafType: LeafType2): BlockLeaves2 {
        return setState<BlockLeaves2>("new_leaf_type", leafType.name.lowercase(Locale.getDefault()))
    }

    val leafType: LeafType2
        get() = if (stateExists("new_leaf_type")) LeafType2.valueOf(getStringState("new_leaf_type")) else LeafType2.ACACIA
}
