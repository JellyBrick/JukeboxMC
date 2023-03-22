package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockPurpurBlock
import org.jukeboxmc.block.data.PurpurType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemPurpurBlock : Item {
    private val block: BlockPurpurBlock

    constructor(identifier: Identifier) : super(identifier) {
        block = Block.create(BlockType.PURPUR_BLOCK)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.create(BlockType.PURPUR_BLOCK)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemPurpurBlock {
        this.blockRuntimeId = blockRuntimeId
        block.blockStates = BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states")
        return this
    }

    fun setPurpurType(purpurType: PurpurType): ItemPurpurBlock {
        blockRuntimeId = block.setPurpurType(purpurType).runtimeId
        return this
    }

    val purpurType: PurpurType
        get() = block.purpurType
}
