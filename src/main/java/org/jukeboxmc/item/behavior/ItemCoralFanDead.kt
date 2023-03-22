package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockCoralFanDead
import org.jukeboxmc.block.data.CoralColor
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemCoralFanDead : Item {
    private val block: BlockCoralFanDead

    constructor(identifier: Identifier?) : super(identifier) {
        block = Block.create<BlockCoralFanDead>(BlockType.CORAL_FAN_DEAD)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.create<BlockCoralFanDead>(BlockType.CORAL_FAN_DEAD)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemCoralFanDead {
        this.blockRuntimeId = blockRuntimeId
        block.setBlockStates(BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states"))
        return this
    }

    fun setCoralColor(coralColor: CoralColor): ItemCoralFanDead {
        blockRuntimeId = block.setCoralColor(coralColor).runtimeId
        return this
    }

    val coralColor: CoralColor
        get() = block.coralColor
}
