package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockStoneSlab3
import org.jukeboxmc.block.data.StoneSlab3Type
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemStoneSlab3 : Item {
    private val block: BlockStoneSlab3

    constructor(identifier: Identifier?) : super(identifier) {
        block = Block.Companion.create<BlockStoneSlab3>(BlockType.STONE_BLOCK_SLAB3)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.Companion.create<BlockStoneSlab3>(BlockType.STONE_BLOCK_SLAB3)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemStoneSlab3 {
        this.blockRuntimeId = blockRuntimeId
        block.blockStates = BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states")
        return this
    }

    fun setStoneType(stoneSlabType: StoneSlab3Type): ItemStoneSlab3 {
        blockRuntimeId = block.setStoneSlabType(stoneSlabType).runtimeId
        return this
    }

    val stoneType: StoneSlab3Type
        get() = block.stoneSlabType
}