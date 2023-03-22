package org.jukeboxmc.item.behavior

import org.jukeboxmc.item.ItemType
import org.jukeboxmc.util.Identifier

/**
 * @author LucGamesYT
 * @version 1.0
 */
class ItemGoldenApple : ItemFood {
    constructor(identifier: Identifier?) : super(identifier)
    constructor(itemType: ItemType) : super(itemType)

    override val saturation: Float
        get() = 9.6f
    override val hunger: Int
        get() = 4
}
