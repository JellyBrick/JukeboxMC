package org.jukeboxmc.item.behavior

import java.time.Duration
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockCarpet
import org.jukeboxmc.block.data.BlockColor
import org.jukeboxmc.item.Burnable
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemCarpet : Item, Burnable {
    private val block: BlockCarpet

    constructor(identifier: Identifier?) : super(identifier) {
        block = Block.Companion.create<BlockCarpet>(BlockType.CARPET)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.Companion.create<BlockCarpet>(BlockType.CARPET)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemCarpet {
        this.blockRuntimeId = blockRuntimeId
        block.blockStates = BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states")
        return this
    }

    fun setColor(blockColor: BlockColor): ItemCarpet {
        blockRuntimeId = block.setColor(blockColor).runtimeId
        return this
    }

    val color: BlockColor
        get() = block.color
    override val burnTime: Duration?
        get() = Duration.ofMillis(67)
}