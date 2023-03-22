package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.LeafType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemLeaves
import org.jukeboxmc.util.Identifier
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockLeaves : Block {
    constructor(identifier: Identifier) : super(identifier)

    override fun toItem(): Item {
        return Item.create<ItemLeaves>(ItemType.LEAVES).setLeafType(
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

    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

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

    fun setLeafType(leafType: LeafType): BlockLeaves {
        return setState<BlockLeaves>("old_leaf_type", leafType.name.lowercase(Locale.getDefault()))
    }

    val leafType: LeafType
        get() = if (stateExists("old_leaf_type")) LeafType.valueOf(getStringState("old_leaf_type")) else LeafType.OAK
}
