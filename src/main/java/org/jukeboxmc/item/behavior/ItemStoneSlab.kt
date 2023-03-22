package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockStoneSlab
import org.jukeboxmc.block.data.StoneSlabType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemStoneSlab : Item {
    private val block: BlockStoneSlab

    constructor(identifier: Identifier?) : super(identifier) {
        block = Block.create<BlockStoneSlab>(BlockType.STONE_BLOCK_SLAB)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.create<BlockStoneSlab>(BlockType.STONE_BLOCK_SLAB)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemStoneSlab {
        this.blockRuntimeId = blockRuntimeId
        block.setBlockStates(BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states"))
        return this
    }

    fun setStoneType(stoneSlabType: StoneSlabType): ItemStoneSlab {
        blockRuntimeId = block.setStoneSlabType(stoneSlabType).runtimeId
        return this
    }

    val stoneType: StoneSlabType
        get() = block.stoneSlabType
}
