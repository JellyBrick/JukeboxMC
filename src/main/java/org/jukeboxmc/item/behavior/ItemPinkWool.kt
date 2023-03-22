package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockPinkWool
import org.jukeboxmc.item.Burnable
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.Identifier
import java.time.Duration

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemPinkWool : Item, Burnable {
    private val block: BlockPinkWool

    constructor(identifier: Identifier?) : super(identifier) {
        block = Block.create<BlockPinkWool>(BlockType.PINK_WOOL)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.create<BlockPinkWool>(BlockType.PINK_WOOL)
        blockRuntimeId = block.runtimeId
    }

    override val burnTime: Duration?
        get() = Duration.ofMillis(100)
}
