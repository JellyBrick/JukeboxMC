package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.item.Burnable
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.Identifier
import java.time.Duration

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemJungleSign : Item, Burnable {
    constructor(identifier: Identifier) : super(identifier)
    constructor(itemType: ItemType) : super(itemType)

    override fun toBlock(): Block {
        return Block.create(BlockType.JUNGLE_STANDING_SIGN)
    }

    override val burnTime: Duration?
        get() = Duration.ofMillis(200)
}
