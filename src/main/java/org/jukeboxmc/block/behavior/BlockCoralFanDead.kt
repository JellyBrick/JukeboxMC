package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.CoralColor
import org.jukeboxmc.block.direction.RotationDirection
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.behavior.ItemCoralFanDead
import org.jukeboxmc.util.Identifier
import java.util.Locale

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockCoralFanDead : Block {
    constructor(identifier: Identifier?) : super(identifier)
    constructor(identifier: Identifier?, blockStates: NbtMap?) : super(identifier, blockStates)

    override fun toItem(): Item {
        return Item.create<ItemCoralFanDead>(ItemType.CORAL_FAN_DEAD).setCoralColor(
            coralColor,
        )
    }

    fun setCoralDirection(rotationDirection: RotationDirection) {
        setState<Block>("coral_fan_direction", rotationDirection.ordinal)
    }

    val rotationDirection: RotationDirection
        get() = if (stateExists("coral_fan_direction")) RotationDirection.values()[getIntState("coral_fan_direction")] else RotationDirection.EAST_WEST

    fun setCoralColor(coralColor: CoralColor): BlockCoralFanDead {
        return setState("coral_color", coralColor.name.lowercase(Locale.getDefault()))
    }

    val coralColor: CoralColor
        get() = if (stateExists("coral_color")) CoralColor.valueOf(getStringState("coral_color")) else CoralColor.BLUE
}
