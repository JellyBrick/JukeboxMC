package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockStainedGlass
import org.jukeboxmc.block.data.BlockColor
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemStainedGlass : Item {
    private val block: BlockStainedGlass

    constructor(identifier: Identifier) : super(identifier) {
        block = Block.create(BlockType.STAINED_GLASS)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.create(BlockType.STAINED_GLASS)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemStainedGlass {
        this.blockRuntimeId = blockRuntimeId
        block.blockStates = BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states")
        return this
    }

    fun setColor(blockColor: BlockColor): ItemStainedGlass {
        blockRuntimeId = block.setColor(blockColor).runtimeId
        return this
    }

    val color: BlockColor
        get() = block.color
}
