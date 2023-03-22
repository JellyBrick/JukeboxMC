package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.CoralColor
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemCoral
import org.jukeboxmc.util.Identifier
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockCoral : Block {
    constructor(identifier: Identifier) : super(identifier)
    constructor(identifier: Identifier, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun toItem(): Item {
        return Item.create<ItemCoral>(ItemType.CORAL).setCoralColor(
            coralColor,
        ).setDead(
            isDead,
        )
    }

    fun setCoralColor(coralColor: CoralColor): BlockCoral {
        return setState("coral_color", coralColor.name.lowercase(Locale.getDefault()))
    }

    val coralColor: CoralColor
        get() = if (stateExists("coral_color")) CoralColor.valueOf(getStringState("coral_color")) else CoralColor.BLUE

    fun setDead(value: Boolean): BlockCoral {
        return setState("dead_bit", if (value) 1.toByte() else 0.toByte())
    }

    val isDead: Boolean
        get() = stateExists("dead_bit") && getByteState("dead_bit").toInt() == 1
}
