package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
open class ItemDoor : Item {
    constructor(identifier: Identifier) : super(identifier)
    constructor(itemType: ItemType) : super(itemType)

    override fun toBlock(): Block {
        return Block.create(BlockType.valueOf(this.type.name))
    }
}
