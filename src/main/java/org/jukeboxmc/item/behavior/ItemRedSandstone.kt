package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockRedSandstone
import org.jukeboxmc.block.data.SandStoneType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemRedSandstone : Item {
    private val block: BlockRedSandstone

    constructor(identifier: Identifier) : super(identifier) {
        block = Block.create(BlockType.RED_SANDSTONE)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.create(BlockType.RED_SANDSTONE)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemRedSandstone {
        this.blockRuntimeId = blockRuntimeId
        block.blockStates = BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states")
        return this
    }

    fun setPrismarineType(sandStoneType: SandStoneType): ItemRedSandstone {
        blockRuntimeId = block.setSandStoneType(sandStoneType).runtimeId
        return this
    }

    val woodType: SandStoneType
        get() = block.sandStoneType
}
