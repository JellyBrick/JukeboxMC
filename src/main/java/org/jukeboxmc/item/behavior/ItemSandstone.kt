package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockSandstone
import org.jukeboxmc.block.data.SandStoneType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemSandstone : Item {
    private val block: BlockSandstone

    constructor(identifier: Identifier) : super(identifier) {
        block = Block.create(BlockType.SANDSTONE)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.create(BlockType.SANDSTONE)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemSandstone {
        this.blockRuntimeId = blockRuntimeId
        block.blockStates = BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states")
        return this
    }

    fun setSandStoneType(sandStoneType: SandStoneType): ItemSandstone {
        blockRuntimeId = block.setSandStoneType(sandStoneType).runtimeId
        return this
    }

    val sandStoneType: SandStoneType
        get() = block.sandStoneType
}
