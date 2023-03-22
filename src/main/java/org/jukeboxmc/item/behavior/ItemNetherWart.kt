package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockNetherWart
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemNetherWart : Item {
    private val block: BlockNetherWart

    constructor(identifier: Identifier?) : super(identifier) {
        block = Block.create<BlockNetherWart>(BlockType.NETHER_WART)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.create<BlockNetherWart>(BlockType.NETHER_WART)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemNetherWart {
        this.blockRuntimeId = blockRuntimeId
        block.setBlockStates(BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states"))
        return this
    }
}
