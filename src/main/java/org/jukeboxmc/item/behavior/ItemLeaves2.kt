package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockLeaves2
import org.jukeboxmc.block.data.LeafType2
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemLeaves2 : Item {
    private val block: BlockLeaves2

    constructor(identifier: Identifier) : super(identifier) {
        block = Block.create<BlockLeaves2>(BlockType.LEAVES2)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.create<BlockLeaves2>(BlockType.LEAVES2)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemLeaves2 {
        this.blockRuntimeId = blockRuntimeId
        block.blockStates = BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states")
        return this
    }

    fun setLeafType(leafType: LeafType2): ItemLeaves2 {
        blockRuntimeId = block.setLeafType(leafType).runtimeId
        return this
    }

    val leafType: LeafType2
        get() = block.leafType
}
