package org.jukeboxmc.blockentity

import com.nukkitx.nbt.NbtMap
import com.nukkitx.nbt.NbtMapBuilder
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.data.BlockColor

/**
 * @author LucGamesYT
 * @version 1.0
 */
class BlockEntityBed(block: Block, blockEntityType: BlockEntityType) : BlockEntity(block, blockEntityType) {
    private var color: Byte = 14
    override fun fromCompound(compound: NbtMap) {
        super.fromCompound(compound)
        color = compound.getByte("color")
    }

    override fun toCompound(): NbtMapBuilder {
        val compound = super.toCompound()
        compound.putByte("color", color)
        return compound
    }

    fun setColor(blockColor: BlockColor): BlockEntityBed {
        color = blockColor.ordinal.toByte()
        return this
    }

    fun getColor(): BlockColor {
        return BlockColor.values()[color.toInt()]
    }
}
