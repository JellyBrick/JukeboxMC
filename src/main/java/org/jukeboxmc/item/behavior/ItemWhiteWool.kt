package org.jukeboxmc.item.behavior

import java.time.Duration
import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockWhiteWool
import org.jukeboxmc.item.Burnable
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemWhiteWool : Item, Burnable {
    private val block: BlockWhiteWool

    constructor(identifier: Identifier?) : super(identifier) {
        block = Block.Companion.create<BlockWhiteWool>(BlockType.WHITE_WOOL)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.Companion.create<BlockWhiteWool>(BlockType.WHITE_WOOL)
        blockRuntimeId = block.runtimeId
    }

    override val burnTime: Duration?
        get() = Duration.ofMillis(100)
}