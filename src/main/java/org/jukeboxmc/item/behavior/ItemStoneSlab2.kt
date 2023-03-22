package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockStoneSlab2
import org.jukeboxmc.block.data.StoneSlab2Type
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemStoneSlab2 : Item {
    private val block: BlockStoneSlab2

    constructor(identifier: Identifier) : super(identifier) {
        block = Block.create(BlockType.STONE_BLOCK_SLAB2)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.create(BlockType.STONE_BLOCK_SLAB2)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemStoneSlab2 {
        this.blockRuntimeId = blockRuntimeId
        block.blockStates = BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states")
        return this
    }

    fun setStoneType(stoneSlabType: StoneSlab2Type): ItemStoneSlab2 {
        blockRuntimeId = block.setStoneSlabType(stoneSlabType).runtimeId
        return this
    }

    val stoneType: StoneSlab2Type
        get() = block.stoneSlabType
}
