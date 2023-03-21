package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockShulkerBox
import org.jukeboxmc.block.data.BlockColor
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemShulkerBox : Item {
    private val block: BlockShulkerBox

    constructor(identifier: Identifier?) : super(identifier) {
        block = Block.Companion.create<BlockShulkerBox>(BlockType.SHULKER_BOX)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.Companion.create<BlockShulkerBox>(BlockType.SHULKER_BOX)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemShulkerBox {
        this.blockRuntimeId = blockRuntimeId
        block.blockStates = BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states")
        return this
    }

    fun setColor(blockColor: BlockColor): ItemShulkerBox {
        blockRuntimeId = block.setColor(blockColor).runtimeId
        return this
    }

    val color: BlockColor
        get() = block.color
}