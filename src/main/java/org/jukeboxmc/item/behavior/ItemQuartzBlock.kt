package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockQuartzBlock
import org.jukeboxmc.block.data.QuartzType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemQuartzBlock : Item {
    private val block: BlockQuartzBlock

    constructor(identifier: Identifier) : super(identifier) {
        block = Block.create(BlockType.QUARTZ_BLOCK)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.create(BlockType.QUARTZ_BLOCK)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemQuartzBlock {
        this.blockRuntimeId = blockRuntimeId
        block.blockStates = BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states")
        return this
    }

    fun setPrismarineType(quartzType: QuartzType): ItemQuartzBlock {
        blockRuntimeId = block.setQuartzType(quartzType).runtimeId
        return this
    }

    val woodType: QuartzType
        get() = block.quartzType
}
