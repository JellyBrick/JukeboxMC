package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockStone
import org.jukeboxmc.block.data.StoneType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemStone : Item {
    private val block: BlockStone

    constructor(identifier: Identifier?) : super(identifier) {
        block = Block.Companion.create<BlockStone>(BlockType.STONE)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.Companion.create<BlockStone>(BlockType.STONE)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemStone {
        this.blockRuntimeId = blockRuntimeId
        block.blockStates = BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states")
        return this
    }

    fun setStoneType(stoneType: StoneType): ItemStone {
        blockRuntimeId = block.setStoneType(stoneType).runtimeId
        return this
    }

    val color: StoneType
        get() = block.stoneType
}