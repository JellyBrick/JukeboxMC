package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockCoralFan
import org.jukeboxmc.block.data.CoralColor
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemCoralFan : Item {
    private val block: BlockCoralFan

    constructor(identifier: Identifier?) : super(identifier) {
        block = Block.create<BlockCoralFan>(BlockType.CORAL_FAN)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.create<BlockCoralFan>(BlockType.CORAL_FAN)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemCoralFan {
        this.blockRuntimeId = blockRuntimeId
        block.setBlockStates(BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states"))
        return this
    }

    fun setCoralColor(coralColor: CoralColor): ItemCoralFan {
        blockRuntimeId = block.setCoralColor(coralColor).runtimeId
        return this
    }

    val coralColor: CoralColor
        get() = block.coralColor
}
