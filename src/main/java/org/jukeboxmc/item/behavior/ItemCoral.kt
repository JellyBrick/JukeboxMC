package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockCoral
import org.jukeboxmc.block.data.CoralColor
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.BlockPalette
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemCoral : Item {
    private val block: BlockCoral

    constructor(identifier: Identifier?) : super(identifier) {
        block = Block.Companion.create<BlockCoral>(BlockType.CORAL)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.Companion.create<BlockCoral>(BlockType.CORAL)
        blockRuntimeId = block.runtimeId
    }

    override fun setBlockRuntimeId(blockRuntimeId: Int): ItemCoral {
        this.blockRuntimeId = blockRuntimeId
        block.blockStates = BlockPalette.getBlockNbt(blockRuntimeId).getCompound("states")
        return this
    }

    fun setCoralColor(coralColor: CoralColor): ItemCoral {
        blockRuntimeId = block.setCoralColor(coralColor).runtimeId
        return this
    }

    val coralColor: CoralColor
        get() = block.coralColor

    fun setDead(value: Boolean): ItemCoral {
        blockRuntimeId = block.setDead(value).runtimeId
        return this
    }

    val isDead: Boolean
        get() = block.isDead
}