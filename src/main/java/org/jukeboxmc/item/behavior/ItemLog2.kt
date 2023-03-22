package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockLog2
import org.jukeboxmc.block.data.LogType2
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemLog2 : Item {
    private val block: BlockLog2

    constructor(identifier: Identifier?) : super(identifier) {
        block = Block.create<BlockLog2>(BlockType.LOG2)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.create<BlockLog2>(BlockType.LOG2)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemLog2 {
        this.blockRuntimeId = blockRuntimeId
        block.setBlockStates(BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states"))
        return this
    }

    fun setLogType(logType2: LogType2): ItemLog2 {
        blockRuntimeId = block.setLogType(logType2).runtimeId
        return this
    }

    val logType: LogType2
        get() = block.logType
}
