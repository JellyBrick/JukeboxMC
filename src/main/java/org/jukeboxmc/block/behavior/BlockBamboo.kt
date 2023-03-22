package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import java.util.Locale
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.BambooLeafSize
import org.jukeboxmc.block.data.BambooStalkThickness
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockBamboo : Block {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    var bambooLeafSize: BambooLeafSize
        get() = if (stateExists("bamboo_leaf_size")) BambooLeafSize.valueOf(getStringState("bamboo_leaf_size")) else BambooLeafSize.NO_LEAVES
        set(bambooLeafSize) {
            setState<Block>("bamboo_leaf_size", bambooLeafSize.name.lowercase(Locale.getDefault()))
        }

    fun setAge(value: Boolean) {
        setState<Block>("age_bit", if (value) 1.toByte() else 0.toByte())
    }

    fun hasAge(): Boolean {
        return stateExists("age_bit") && getByteState("age_bit").toInt() == 1
    }

    var bambooStalkThickness: BambooStalkThickness
        get() = if (stateExists("bamboo_stalk_thickness")) BambooStalkThickness.valueOf(getStringState("bamboo_stalk_thickness")) else BambooStalkThickness.THIN
        set(bambooStalkThickness) {
            setState<Block>("bamboo_stalk_thickness", bambooStalkThickness.name.lowercase(Locale.getDefault()))
        }
}
