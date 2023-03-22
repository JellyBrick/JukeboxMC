package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockLeaves
import org.jukeboxmc.block.data.LeafType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemLeaves : Item {
    private val block: BlockLeaves

    constructor(identifier: Identifier) : super(identifier) {
        block = Block.create<BlockLeaves>(BlockType.LEAVES)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.create<BlockLeaves>(BlockType.LEAVES)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemLeaves {
        this.blockRuntimeId = blockRuntimeId
        block.blockStates = BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states")
        return this
    }

    fun setLeafType(leafType: LeafType): ItemLeaves {
        blockRuntimeId = block.setLeafType(leafType).runtimeId
        return this
    }

    val leafType: LeafType
        get() = block.leafType
}
