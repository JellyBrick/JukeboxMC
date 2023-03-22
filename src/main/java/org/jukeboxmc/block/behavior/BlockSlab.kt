package org.jukeboxmc.block.behavior

import com.nukkitx.nbt.NbtMap
import org.jukeboxmc.block.Block
import org.jukeboxmc.math.AxisAlignedBB
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
open class BlockSlab : Block {
    constructor(identifier: Identifier?) : super(identifier)
    constructor(identifier: Identifier?, blockStates: NbtMap?) : super(identifier, blockStates)

    override val boundingBox: AxisAlignedBB
        get() = if (isTopSlot) {
            AxisAlignedBB(
                location.getX(),
                location.getY() + 0.5f,
                location.getZ(),
                location.getX() + 1,
                location.getY() + 1,
                location.getZ() + 1,
            )
        } else {
            AxisAlignedBB(
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getX() + 1,
                location.getY() + 0.5f,
                location.getZ() + 1,
            )
        }

    fun setTopSlot(value: Boolean): BlockSlab {
        setState<Block>("top_slot_bit", if (value) 1.toByte() else 0.toByte())
        return this
    }

    val isTopSlot: Boolean
        get() = stateExists("top_slot_bit") && getByteState("top_slot_bit").toInt() == 1
}
