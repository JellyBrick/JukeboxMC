package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockDirt
import org.jukeboxmc.block.data.DirtType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemDirt : Item {
    private val block: BlockDirt

    constructor(identifier: Identifier) : super(identifier) {
        block = Block.create(BlockType.DIRT)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.create(BlockType.DIRT)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemDirt {
        this.blockRuntimeId = blockRuntimeId
        block.blockStates = BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states")
        return this
    }

    fun setDirtType(dirtType: DirtType): ItemDirt {
        blockRuntimeId = block.setDirtType(dirtType).runtimeId
        return this
    }

    val color: DirtType
        get() = block.dirtType
}
