package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockSand
import org.jukeboxmc.block.data.SandType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemSand : Item {
    private val block: BlockSand

    constructor(identifier: Identifier) : super(identifier) {
        block = Block.create<BlockSand>(BlockType.SAND)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.create<BlockSand>(BlockType.SAND)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemSand {
        this.blockRuntimeId = blockRuntimeId
        block.blockStates = BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states")
        return this
    }

    fun setSandType(sandType: SandType): ItemSand {
        blockRuntimeId = block.setSandType(sandType).runtimeId
        return this
    }

    val sandType: SandType
        get() = block.sandType
}
