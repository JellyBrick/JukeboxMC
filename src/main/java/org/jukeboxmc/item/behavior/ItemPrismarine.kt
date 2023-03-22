package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockPrismarine
import org.jukeboxmc.block.data.PrismarineType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemPrismarine : Item {
    private val block: BlockPrismarine

    constructor(identifier: Identifier) : super(identifier) {
        block = Block.create(BlockType.PRISMARINE)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.create(BlockType.PRISMARINE)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemPrismarine {
        this.blockRuntimeId = blockRuntimeId
        block.blockStates = BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states")
        return this
    }

    fun setPrismarineType(prismarineType: PrismarineType): ItemPrismarine {
        blockRuntimeId = block.setPrismarineType(prismarineType).runtimeId
        return this
    }

    val woodType: PrismarineType
        get() = block.prismarineType
}
