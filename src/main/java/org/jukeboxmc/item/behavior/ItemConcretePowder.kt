package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockConcretePowder
import org.jukeboxmc.block.data.BlockColor
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemConcretePowder : Item {
    private val block: BlockConcretePowder

    constructor(identifier: Identifier?) : super(identifier) {
        block = Block.Companion.create<BlockConcretePowder>(BlockType.CONCRETE_POWDER)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.Companion.create<BlockConcretePowder>(BlockType.CONCRETE_POWDER)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemConcretePowder {
        this.blockRuntimeId = blockRuntimeId
        block.blockStates = BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states")
        return this
    }

    fun setColor(blockColor: BlockColor): ItemConcretePowder {
        blockRuntimeId = block.setColor(blockColor).runtimeId
        return this
    }

    val color: BlockColor
        get() = block.color
}