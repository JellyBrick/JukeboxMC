package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockStainedGlassPane
import org.jukeboxmc.block.data.BlockColor
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemStainedGlassPane : Item {
    private val block: BlockStainedGlassPane

    constructor(identifier: Identifier?) : super(identifier) {
        block = Block.create<BlockStainedGlassPane>(BlockType.STAINED_GLASS_PANE)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.create<BlockStainedGlassPane>(BlockType.STAINED_GLASS_PANE)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemStainedGlassPane {
        this.blockRuntimeId = blockRuntimeId
        block.setBlockStates(BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states"))
        return this
    }

    fun setColor(blockColor: BlockColor): ItemStainedGlassPane {
        blockRuntimeId = block.setColor(blockColor).runtimeId
        return this
    }

    val color: BlockColor
        get() = block.color
}
