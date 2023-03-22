package org.jukeboxmc.item.behavior

import org.jukeboxmc.block.Block
import org.jukeboxmc.block.BlockType
import org.jukeboxmc.item.Item
import org.jukeboxmc.item.ItemType
import org.jukeboxmc.item.SkullType
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemSkull : Item {
    constructor(identifier: Identifier) : super(identifier)
    constructor(itemType: ItemType) : super(itemType)

    override fun toBlock(): Block {
        return Block.create(BlockType.SKULL)
    }

    var skullType: SkullType
        get() = SkullType.values()[meta]
        set(skullType) {
            setMeta(skullType.ordinal)
        }
}
