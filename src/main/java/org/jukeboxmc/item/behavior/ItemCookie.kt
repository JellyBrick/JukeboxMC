package org.jukeboxmc.item.behavior

import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemCookie : ItemFood {
    constructor(identifier: Identifier) : super(identifier)
    constructor(itemType: ItemType) : super(itemType)

    override val saturation: Float
        get() = 0.4f
    override val hunger: Int
        get() = 2
}
