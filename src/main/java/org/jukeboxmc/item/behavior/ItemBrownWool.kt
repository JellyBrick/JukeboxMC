package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.block.behavior.BlockBrownWool
import org.jukeboxmc.item.Burnable
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.Identifier
import java.time.Duration

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemBrownWool : Item, Burnable {
    private val block: BlockBrownWool

    constructor(identifier: Identifier?) : super(identifier) {
        block = Block.create<BlockBrownWool>(BlockType.BROWN_WOOL)
        blockRuntimeId = block.runtimeId
    }

    constructor(itemType: ItemType) : super(itemType) {
        block = Block.create<BlockBrownWool>(BlockType.BROWN_WOOL)
        blockRuntimeId = block.runtimeId
    }

    override val burnTime: Duration?
        get() = Duration.ofMillis(100)
}
