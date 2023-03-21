package org.jukeboxmc.item.behavior

import java.time.Duration
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockSapling
import org.jukeboxmc.block.data.SaplingType
import org.jukeboxmc.item.Burnable
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemSapling : Item, Burnable {
    private val block: BlockSapling

    constructor(identifier: Identifier?) : super(identifier) {
        block = Block.Companion.create<BlockSapling>(BlockType.SAPLING)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.Companion.create<BlockSapling>(BlockType.SAPLING)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemSapling {
        this.blockRuntimeId = blockRuntimeId
        block.blockStates = BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states")
        return this
    }

    fun setSaplingType(saplingType: SaplingType): ItemSapling {
        blockRuntimeId = block.setSaplingType(saplingType).runtimeId
        return this
    }

    val logType: SaplingType
        get() = block.saplingType
    override val burnTime: Duration?
        get() = Duration.ofMillis(100)
}