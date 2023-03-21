package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockStonebrick
import org.jukeboxmc.block.data.StoneBrickType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemStonebrick : Item {
    private val block: BlockStonebrick

    constructor(identifier: Identifier?) : super(identifier) {
        block = Block.Companion.create<BlockStonebrick>(BlockType.STONEBRICK)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.Companion.create<BlockStonebrick>(BlockType.STONEBRICK)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemStonebrick {
        this.blockRuntimeId = blockRuntimeId
        block.blockStates = BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states")
        return this
    }

    fun setStoneBrickType(stoneBrickType: StoneBrickType): ItemStonebrick {
        blockRuntimeId = block.setStoneBrickType(stoneBrickType).runtimeId
        return this
    }

    val stoneBrickType: StoneBrickType
        get() = block.stoneBrickType
}