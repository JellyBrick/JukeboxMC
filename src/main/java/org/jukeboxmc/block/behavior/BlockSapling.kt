package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.SaplingType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemSapling
import org.jukeboxmc.util.Identifier
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockSapling : Block {
    constructor(identifier: Identifier?) : super(identifier)
    constructor(identifier: Identifier?, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun toItem(): Item {
        return Item.create<ItemSapling>(ItemType.SAPLING).setSaplingType(
            saplingType,
        )
    }

    fun setSaplingType(saplingType: SaplingType): BlockSapling {
        return setState("sapling_type", saplingType.name.lowercase(Locale.getDefault()))
    }

    val saplingType: SaplingType
        get() = if (stateExists("sapling_type")) SaplingType.valueOf(getStringState("sapling_type")) else SaplingType.OAK

    fun setAge(value: Boolean) {
        setState<Block>("age_bit", if (value) 1.toByte() else 0.toByte())
    }

    fun hasAge(): Boolean {
        return stateExists("age_bit") && getByteState("age_bit").toInt() == 1
    }
}
